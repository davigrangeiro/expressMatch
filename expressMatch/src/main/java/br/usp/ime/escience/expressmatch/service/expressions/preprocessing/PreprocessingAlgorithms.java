/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.usp.ime.escience.expressmatch.service.expressions.preprocessing;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import br.usp.ime.escience.expressmatch.model.Expression;
import br.usp.ime.escience.expressmatch.model.Point;
import br.usp.ime.escience.expressmatch.model.Stroke;
import br.usp.ime.escience.expressmatch.model.Symbol;

/**
 *
 * @author frank
 */
public class PreprocessingAlgorithms {
    private static final double percentageOfLength=0.9;
    private static final double turningAngleThreashold=(85.0/180)*Math.PI;
    private static final double alphaForTurningAngles=0.12;

    public static Expression preprocessExpression(Expression expression){
        Expression newExpression = new Expression();
        for (Symbol symbol : expression.getSymbols()) {
            Symbol newSymbol=preprocessSymbol(symbol);
            newExpression.addCheckingBoundingBox(newSymbol);
        }
        return newExpression;
    }

    public static Symbol preprocessSymbol(Symbol s){
        Symbol newSymbol=new Symbol();
        for (Stroke stroke : s.getStrokes()) {
                ArrayList<Point> array=strokeToArrayList(stroke);
                array =getNotDuplicatedPoints(array);
                array=equalLengthResampling(array);
                if(array.size()>=3){
                    array=dehooking(array);
                    array=smooth(array);
                    array=equalLengthResampling(array);
                }
                Stroke newStroke= new Stroke();
                for (Point point : array) {
                    newStroke.addCheckingBoundingBox(new Point(point.getX(), point.getY()));
                }
                newSymbol.addCheckingBoundingBox(newStroke);
        }
        return newSymbol;
    }

    public static Point[] getNPoints(Symbol s,int N){
        Point[] points=new Point[N];
        ArrayList<Point> alPoints=new ArrayList<Point>();
//        int pointsPerStroke=(int) Math.round((double)N/s.size());
//        int numStrokes=s.size();
//        int total=0;

        int[] pointsPerStroke = extractNNumberOfPointsPerStroke(s, N);

        int i = 0;
        for(Stroke stroke: s.getStrokes()){
        
//            Point[] pts=getNPoints(s.get(i), pointsPerStroke);
//            alPoints.addAll(Arrays.asList(pts));
//            total+=pointsPerStroke;
            ArrayList<Point> pts= getNPoints(stroke, pointsPerStroke[i++]);
            alPoints.addAll(pts);
        }

//        int rest=N-total;
//        Point[] pts=getNPoints(s.get(numStrokes-1), rest);
//        alPoints.addAll(Arrays.asList(pts));

        for (i = 0; i < points.length; i++) {
            points[i]=alPoints.get(i);

        }
        return points;
    }

    public static int getNumberOfPoints(Symbol s){
        int numberOfPooints=0;
        for (Stroke stroke : s.getStrokes()) {
            numberOfPooints += stroke.getPoints().size();
        }
        return numberOfPooints;
    }

    public static int[] extractNNumberOfPointsPerStroke(Symbol s,int N){
        int totalPoints=getNumberOfPoints(s);
        int[] numberOfPoints = new int[s.getStrokes().size()];
//        double[] exactNumbers=new double[s.size()];
        int index=0;
        double exactNumber=0.;
        for (Stroke stroke : s.getStrokes()) {
            exactNumber=stroke.getPoints().size()*N/(double)totalPoints;
            if(exactNumber<1.||stroke.getPoints().size()==1)
                numberOfPoints[index]=1;
            else
                numberOfPoints[index]=(int) Math.floor(exactNumber);
            index++;
        }
       int suma=sum(numberOfPoints);
       while(suma!=N){
           int max=numberOfPoints[0];
           int posMax=0;
           for (int i = 1; i < numberOfPoints.length; i++) {
               if(max<numberOfPoints[i]){
                   max=numberOfPoints[i];
                   posMax=i;
               }
           }
           if(suma<N){
               numberOfPoints[posMax]++;
               suma++;
           }else{
               numberOfPoints[posMax]--;
               suma--;
           }
       }
        return numberOfPoints;
    }

    public static int sum(int[] numbers){
        int sum=0;
        for (int i = 0; i < numbers.length; i++) {
            sum+= numbers[i];
        }
        return sum;
    }

    public static  ArrayList<Point> getNPoints(Stroke stroke,int N){
        ArrayList<Point> points=new ArrayList<Point>();
        if(N > stroke.getPoints().size()){
            points=strokeToArrayList(stroke);
            completePoints(points, N);
        }
        else{
            double pos=0;
            double distance=(double)stroke.getPoints().size()/N;
            for (int i = 0; i < N; i++) {
                if(pos>=stroke.getPoints().size())
                    points.add(stroke.getPoints().get(stroke.getPoints().size()-1));
                else{
                    points.add(stroke.getPoints().get((int)Math.round(pos)));
                    pos+=distance;
                }
            }
        }
        return points;
    }


    public static void completePoints(ArrayList<Point> points,int N){
        int diference=N-points.size();
        for (int i = 0; i <diference; i++) {
            interpolateAPoint(points);
        }
    }


    public static void interpolateAPoint(ArrayList<Point> points){
        int pos=getPosMaxDistance(points);
        int startPoint=pos;
        int finalPoint=pos+1;
        double distance=points.get(startPoint).distance(points.get(finalPoint));
        Point newPoint= getNewPoint(points.get(startPoint), points.get(finalPoint), distance/2.);
        points.add(finalPoint, newPoint);
    }

    public static int getPosMaxDistance(ArrayList<Point> points){
        int pos=-1;
        double maxDistance=-1;
        double distance=-1;
        int lastPosition=points.size()-1;
        for (int i = 0; i < lastPosition; i++) {
            distance=points.get(i).distance(points.get(i+1));
            if(maxDistance<distance){
                pos=i;
                maxDistance=distance;
            }
        }
        return pos;
    }

    public static Symbol resampleSymbol(Symbol s){
        Symbol resampleSymbol=new Symbol();
        for (Stroke stroke : s.getStrokes()) {
            ArrayList<Point> array=strokeToArrayList(stroke);
            array=getNotDuplicatedPoints(array);
            array=equalLengthResampling(array);
            Stroke newStroke=(Stroke) arrayToStroke(array);
            resampleSymbol.addCheckingBoundingBox(newStroke);
        }
        return resampleSymbol;
    }

    public static Symbol dehookSymbol(Symbol s){
        Symbol resampleSymbol=new Symbol();
        for (Stroke stroke : s.getStrokes()) {
            ArrayList<Point> array=strokeToArrayList(stroke);
            if(array.size()>=3)
                array=dehooking(array);
            Stroke newStroke=(Stroke) arrayToStroke(array);
            resampleSymbol.addCheckingBoundingBox(newStroke);
        }
        return resampleSymbol;
    }

    public static ArrayList<Point> dehooking(ArrayList<Point> s){

        ArrayList<Double> tAngles=turningAngles(s);
        ArrayList<Point> dehooked=new ArrayList<Point>();
        dehooked.add(s.get(0));
        for (int i=0;i<tAngles.size();i++) {
            if(!highAngle(tAngles.get(i))||
                    !isAtExtreme(i+1, s))
                dehooked.add(s.get(i+1));
//            else
//                System.out.println("eliminado: " + tAngles.get(i) + "angle at pos : " + i);
        }
        dehooked.add(s.get(s.size()-1));
        return dehooked;
    }

    public static boolean highAngle(double angle){
        if(angle>turningAngleThreashold)
            return true;
        return false;
    }

    public static boolean isAtExtreme(int pos,ArrayList<Point> s){
        double lengthOfStroke=getDistance(s, 0, s.size()-1);
        if(getDistance(s, 0, pos)<lengthOfStroke*alphaForTurningAngles||
                getDistance(s, pos, s.size()-1)<lengthOfStroke*alphaForTurningAngles)
            return true;
        return false;
    }
    
    /**
     * Returns the Smooth version of Symbol s. s is not modified
     * @param s
     */
    public static Symbol smooth(Symbol s){
        Symbol s2 = new Symbol();
        for (Stroke stro : s.getStrokes()) {
            if(stro.getPoints().size()>=3)
                s2.addCheckingBoundingBox(smooth(stro));
            else
                s2.addCheckingBoundingBox(stro);
        }
        return s2;
    }

    /**
     * Returns the Smooth version of Stroke s. s is not modified
     * @param s
     */
    public static Stroke smooth(Stroke s){
        double [] coefficients={0.25,0.5,0.25};
        int center=1; //central possition of coefficients vector
        Stroke s2 = new Stroke();

        float newX=0;
        float newY=0;
        s2.addCheckingBoundingBox( new Point(s.getPoints().get(0).getX(),s.getPoints().get(0).getY()) );
        
        for(int i=1;i<=(s.getPoints().size()-2);i++){
            newX=0;
            newY=0;
            for(int j=-1;j<=1;j++){
                newX+=(s.getPoints().get(i+j).getX()*coefficients[center+j]);
                newY+=(s.getPoints().get(i+j).getY()*coefficients[center+j]);
            }
            Point timePoint= new Point(newX,newY);
            s2.addCheckingBoundingBox(timePoint);
        }
        s2.addCheckingBoundingBox(new Point(s.getPoints().get(s.getPoints().size()-1).getX(),
        									s.getPoints().get(s.getPoints().size()-1).getY()));
        return s2;
    }

    /**
     * Returns the Smooth version of Stroke s. s is not modified
     * @param s
     */
    public static ArrayList<Point> smooth(ArrayList<Point> s){
        double [] coefficients={0.25,0.5,0.25};
        int center=1; //central possition of coefficients vector
        ArrayList<Point> s2=new ArrayList<Point>();

        float newX=0;
        float newY=0;
        s2.add( new Point(s.get(0).getX(), s.get(0).getY()));
        for(int i=1;i<=(s.size()-2);i++){
            newX=0;
            newY=0;
            for(int j=-1;j<=1;j++){
                newX+=(s.get(i+j).getX()*coefficients[center+j]);
                newY+=(s.get(i+j).getY()*coefficients[center+j]);
            }
            Point timePoint=new Point(newX,newY);
            s2.add(timePoint);
        }
        s2.add(new Point(s.get(s.size()-1).getX(), s.get(s.size()-1).getY()));
        
        return s2;
    }




    /**
     * Calculates the turning angles of s. S must have
     * size greater or equal than 3
     * @param s
     * @return
     */
    public static ArrayList<Double> turningAngles(ArrayList<Point> s){

        ArrayList<Double> tAngles=new ArrayList<Double>(s.size()-2);

        for (int i=1;i<s.size()-1;i++) {

            double dx1=s.get(i).getX()-s.get(i-1).getX();
            double dy1=s.get(i).getY()-s.get(i-1).getY();
            double dx2=s.get(i+1).getX()-s.get(i).getX();
            double dy2=s.get(i+1).getY()-s.get(i).getY();
            double turningAngle=angleBetween2Lines(new Line2D.Double(0,0,dx1,dy1),
                    new Line2D.Double(0,0,dx2,dy2));
            tAngles.add(turningAngle);
        }
        return tAngles;
    }

    public static double angleBetween2Lines(Line2D line1, Line2D line2)
    {
//        double angle1 = Math.atan2(line1.getY2() - line1.getY1(),
//                                   line1.getX2() - line1.getX1());
//        double angle2 = Math.atan2(line2.getY2() - line2.getY1(),
//                                   line2.getX2() - line2.getX1());
        double angle1 = angle(line1);
        double angle2 = angle(line2);
        return Math.abs(angle1-angle2);
    }

    public static double angle(Line2D l){
        double angle=0;
        double dx = (l.getX2()-l.getX1());
        double dy = (l.getY2()-l.getY1());
        double cos=dx/l.getP1().distance(l.getP2());
        angle=Math.acos(cos);
        if(dy>0)
            angle=2.0*Math.PI-angle;

//                float dy = (float)(vertexList[j].getY() - vertexList[i].getY());
//                float modv = dist[i][j]; //(float)fe.euclideanDistance(x1,y1, x2,y2);
//                float cos_theta = dx / modv; //adjacente/hipotenusa
//                float theta = (float)Math.acos(cos_theta);
//                if (dy > 0){
//                    theta = 2.0f*(float)Math.PI - theta;
//                }
        return angle;
    }

    public static ArrayList<Point> strokeToArrayList(Stroke stroke){
        ArrayList<Point> arrayList=new ArrayList<Point>();
        for (Point point : stroke.getPoints()) {
            arrayList.add(point);
        }
        return arrayList;
    }
    public static Stroke  arrayToStroke(ArrayList<Point> array){
        Stroke stroke=new Stroke();
        for (Point point : array) {
            stroke.addCheckingBoundingBox(new Point(point.getX(), point.getY()) );
        }
        return stroke;
    }

    public static ArrayList<Point> equalLengthResampling(ArrayList<Point> points){
        ArrayList<Point> resampledPoints=new ArrayList<Point>();
        ArrayList<Point> copyOfPoints=new ArrayList<Point>();

        for (int i = 0; i < points.size(); i++) {
            copyOfPoints.add(new Point(points.get(i).getX(),points.get(i).getY()));
        }


        if(copyOfPoints.size()>1){
            resampledPoints.add(new Point(copyOfPoints.get(0).getX(),
                    copyOfPoints.get(0).getY()));
            double length=getLength(points);
            double dist=0;
            int i=0;
            int j=i+1;
            while (true) {
//                if(j==(copyOfPoints.size()-1)){
//                    resampledPoints.add(copyOfPoints.get(i));
//                    break;
//                }
                dist=getDistance(copyOfPoints, i, j);
                if(length<dist){
                    Point newPoint=getNewPoint(copyOfPoints.get(i), copyOfPoints.get(j),length);
                    
                    resampledPoints.add(newPoint);
                    copyOfPoints.get(i).setLocation(newPoint.getX(),newPoint.getY());
                }
                 else{
                    while(length>=dist&&j<copyOfPoints.size()){
                        dist=getDistance(copyOfPoints, i, j);
                        j++;
                    }
                    j--;
                    if(length>=dist){
                        resampledPoints.add(new Point(copyOfPoints.get(j).getX(),copyOfPoints.get(j).getY()));
                        break;
                    }
//                    double Ldif=dist-length;
                    double Ldif=length-getDistance(copyOfPoints, i, j-1);;
                    Point newPoint=getNewPoint(copyOfPoints.get(j-1),
                            copyOfPoints.get(j),Ldif);
                    resampledPoints.add(newPoint);
                    i=j-1;
                    copyOfPoints.get(i).setLocation(newPoint.getX(),newPoint.getY());
                 }
            }
        }
     else{
            for (Point point2D : copyOfPoints) {
                resampledPoints.add(point2D);
            }
     }
        return resampledPoints;
    }

    public static double getLength(ArrayList<Point> points){
        double totalLength=getDistance(points, 0, points.size()-1);
        return percentageOfLength*totalLength/points.size();
    }

    public static Point getNewPoint(Point p1,Point p2, double d){
        Point newPoint=new Point();
        Double newX=getNewX(p1, p2, d);
        Double newY=getNewY(newX, p1, p2, d);
        newPoint.setLocation(newX.floatValue(), newY.floatValue());
        return newPoint;
    }

    public static ArrayList<Point> getNotDuplicatedPoints(ArrayList<Point> points){
        ArrayList<Point> notDuplicatedPoints=new ArrayList<Point>(points.size());
        for (int i = 0; i < points.size(); i++)
            if(!notDuplicatedPoints.contains(points.get(i)))
                notDuplicatedPoints.add(new Point(points.get(i).getX(),
                        points.get(i).getY()));
        return notDuplicatedPoints;
    }

    public static double getNewX(Point p1,Point p2, double d){
        if(p1.getX()==p2.getX())
            return p1.getX();
        double k=(p2.getY()-p1.getY())/(p2.getX()-p1.getX());
        if(p1.getX()<p2.getX())
            return p1.getX()+Math.sqrt(d*d/(k*k+1));
        return p1.getX()-Math.sqrt(d*d/(k*k+1));
    }

    public static double getNewY(double X,Point p1,Point p2, double d){
        if(p1.getX()!=p2.getX()){
            double k=(p2.getY()-p1.getY())/(p2.getX()-p1.getX());
            return X*k+p1.getY()-k*p1.getX();
        }
        if(p1.getY()<p2.getY())
            return p1.getY()+d;
        return p1.getY()-d;
    }

    public static double getDistance(ArrayList<Point> points,int first,int last){
        double distance=0;
        for (int i = first; i < last; i++) {
            distance+= points.get(i).distance(points.get(i+1));
        }
        return distance;
    }

}
