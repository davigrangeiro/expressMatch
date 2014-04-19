/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.usp.ime.escience.expressmatch.service.expressions.preprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import br.usp.ime.escience.expressmatch.model.Point;
import br.usp.ime.escience.expressmatch.model.Stroke;
import br.usp.ime.escience.expressmatch.model.Symbol;



/**
 * THIS CLASS COULD BE USED IN THE FUTURE TO EXTRACT FEATURES
 * THIS CLASS WILL CHANGE AS THE MATCHING ALGORITHMS COULD NEED IT
 * @author frank
 * This class implements the feature extractor described by Ernesto Tapia in his
 * doctorals thesis (Germany)
 */
public class FETapia {


    private static int maxNumberOfStrokes=3;
    public static int numberOfFeatures=63;

    /**THIS IS NOT USED YET
     * Returns the Smooth version of Stroke s. s is not modified
     * @param s
     */
    public static Stroke smooth(Stroke s){
        double [] coefficients={0.25,0.5,0.25};
        int center=1; //central possition of coefficients vector
        Stroke s2=new Stroke();
        
        Double newX= 0.0;
        Double newY= 0.0;
        s2.addCheckingBoundingBox(s.getPoints().get(0));
        for(int i=1;i<(s.getPoints().size()-1);i++){
            newX= 0.0;
            newY= 0.0;
            for(int j=-1;j<=1;j++){
                newX+=(s.getPoints().get(i+j).getX()*coefficients[center+j]);
                newY+=(s.getPoints().get(i+j).getY()*coefficients[center+j]);
            }
            Point timePoint=new Point(newX.floatValue(),newY.floatValue());
            s2.addCheckingBoundingBox(timePoint);
        }
        s2.addCheckingBoundingBox(s.getPoints().get(s.getPoints().size()-1));
        return s2;
    }

    /**
     * THIS IS NOT USED YET
     * Returns the Smooth version of Symbol s. s is not modified
     * @param s
     */
    public static Symbol smooth(Symbol s){
        Symbol s2=new Symbol();
        for (Stroke stro : s.getStrokes()) {
            s2.addCheckingBoundingBox(smooth(stro));
        }
        return s2;
    }


    /**
     * Calculates the turning angles of yhe points
     * as described by Tapia's Thesis
     * @param s
     * @return
     */
    public static double[] turningAngles(Point[] s){

        double[] tAngles=new double[2*(s.length-1)];

        for (int i=0;i<s.length-1;i++) {

            double dist=s[i].distance(s[i+1]);
            if(dist!=0){

                tAngles[i]=(s[i+1].getX()-s[i].getX())/dist;
                tAngles[s.length-1-i]=(s[i+1].getY()-s[i].getY())/dist;
            }
            else{
                tAngles[i]=Math.cos(Math.PI*2);
                tAngles[s.length-1-i]=Math.sin(Math.PI*2);
            }
        }
        return tAngles;
    }


    /**
     * it works ust when S.SIZE()>=N
     * @param s
     * @param N
     * @return
     */
    public static Point[] getNPoints(Stroke s,int N){
        Point[] points=new Point[N];
        if(s.getPoints().size()>=N){
            double pos=0;
            double distance=(double)s.getPoints().size()/N;
            for (int i = 0; i < points.length; i++) {
                points[i]=s.getPoints().get((int)pos);
                pos+=distance;
            }

        }else{

           if(s.getPoints().size()>1){
               Point[] pts=new Point[s.getPoints().size()];
               int i=0;
                for (Point point2D : s.getPoints()) {
                    Point newP=new Point();
                    newP.setLocation(point2D.getX(),point2D.getY());
                    pts[i++]=newP;
                }
                points=arcLengthResampling(pts, N);
           }
           else{
               for (int i = 0; i < N; i++) {
                   points[i]=new Point(s.getPoints().get(0).getX(), s.getPoints().get(0).getY());
               }
           }
        }

        return points;
    }

    /**
     * NOT USED YET
     * @param s
     * @param m
     * @return
     */
    public static Symbol arcLnegthResampling(Symbol s, int m){
       Symbol newS=new Symbol();
       for(int i=0;i<s.getStrokes().size();i++){
           Stroke str=arcLnegthResampling(s.getStrokes().get(i), m);
           newS.addCheckingBoundingBox(str);
       }
       return newS;
    }

    /**
     * NOT USED YET
     * @param str
     * @param m
     * @return
     */
    public static Stroke arcLnegthResampling(Stroke str, int m){

       Stroke s=new Stroke();
       Point[] pts=new Point[str.getPoints().size()];
       int i=0;
        for (Point point2D : str.getPoints()) {
            Point newP=new Point();
            newP.setLocation(point2D.getX(),point2D.getY());
            pts[i++]=newP;
        }

       Point[] newPts=arcLengthResampling(pts, m);
        for (Point point2D : newPts) {
            s.addCheckingBoundingBox(new Point(point2D));
        }


       return s;
    }

    /**
     * NOT USED YET
     * @param p
     * @param m
     * @return
     */
    public static Point[] arcLengthResampling(Point[] p, int m){
        Point[] newPoints=new Point[m];
        int k=0,i;
        newPoints[k]=p[0];
        newPoints[m-1]=p[p.length-1];
        double sum=0;//,sum2=0;
        double L=0;
        for (int j = 0; j < (p.length-1); j++) {
            L+=p[j+1].distance(p[j]);
        }
        int pos=1;
        Point newP=p[0];
        while(k<(p.length-1)&&pos<(m-1)){
            sum=p[k+1].distance(newP);
            //sum=p[k+1].distance(p[k]);
            for(i=k+1;i<(p.length-1);){
                if(sum>(double)L/(m-1)){
                    sum-=p[i+1].distance(p[i]);
                    k=i;
                    break;
                }
                i++;
                if(i<(p.length-1))
                    sum+=p[i+1].distance(p[i]);

            }
            newP=new Point();
            Double dist=Math.sqrt(L/(double)(p.length-1)-sum);
            newP.setLocation(p[k].getX()+dist.floatValue(),p[k].getY()+dist.floatValue());
            newPoints[pos++]=newP;


        }
        //System.out.println("final : "+pos);

        return newPoints;
    }


    public static Point[] getNPoints(Symbol s,int N){
        Point[] points=new Point[N];
        ArrayList<Point> alPoints=new ArrayList<Point>();
        int pointsPerStroke=(int) Math.round((double)N/s.getStrokes().size());
        int numStrokes=s.getStrokes().size();
        int total=0;

        for (int i = 0; i < (numStrokes-1); i++) {
            Point[] pts=getNPoints(s.getStrokes().get(i), pointsPerStroke);
            alPoints.addAll(Arrays.asList(pts));
            total+=pointsPerStroke;
        }

        int rest=N-total;
        Point[] pts=getNPoints(s.getStrokes().get(numStrokes-1), rest);
        alPoints.addAll(Arrays.asList(pts));

        for (int i = 0; i < points.length; i++) {
            points[i]=alPoints.get(i);

        }
        return points;
    }

    public static int getNumberOfPoints(Symbol s){
        int numberOfPooints=0;
        for (Stroke stroke : s.getStrokes()) {
            numberOfPooints+=stroke.getPoints().size();
        }
        return numberOfPooints;
    }

    public static int[] extractNNumberOfPointsPerStroke(Symbol s,int N){
        int totalPoints=getNumberOfPoints(s);
        int[] numberOfPoints=new int[s.getStrokes().size()];
//        double[] exactNumbers=new double[s.size()];
        int index=0;
        double exactNumber=0.;
        for (Stroke stroke : s.getStrokes()) {
            exactNumber=stroke.getPoints().size()*N/(double)totalPoints;
            if(exactNumber<1.)
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

    public static Point[] getNProportionalPoints(Symbol s,int N){
        Point[] points=new Point[N];
        ArrayList<Point> alPoints=new ArrayList<Point>();
//        int pointsPerStroke=(int) Math.round((double)N/s.size());
//        int numStrokes=s.size();
//        int total=0;

        int[]pointsPerStroke=extractNNumberOfPointsPerStroke(s, N);

        for (int i = 0; i < pointsPerStroke.length; i++) {
//            Point[] pts=getNPoints(s.get(i), pointsPerStroke);
//            alPoints.addAll(Arrays.asList(pts));
//            total+=pointsPerStroke;
            Point[] pts=getNPoints(s.getStrokes().get(i), pointsPerStroke[i]);
            alPoints.addAll(Arrays.asList(pts));
        }

//        int rest=N-total;
//        Point[] pts=getNPoints(s.get(numStrokes-1), rest);
//        alPoints.addAll(Arrays.asList(pts));

        for (int i = 0; i < points.length; i++) {
            points[i]=alPoints.get(i);

        }
        return points;
    }

    /**
     * Recives a vector of objects of the clas Point and returns a vector
     * that contains just the Xs and Ys coordenates of those points
     * @param p
     * @return
     */
    public static double [] formatPoints(Point[] p){
        double [] points=new double[p.length*2];
        for (int i=0;i<p.length;i++) {
            points[i]=p[i].getX();
            points[p.length*2-1-i]=p[i].getY();
        }
        return points;
    }

    /**
     * This method is designed to work with Symbols with any number of strokes
     * and the features are; points of strokes and turning angle
     * @param s
     * @return
     */
    public static double[] extractFeatures(Symbol symbol){
        int numberOfPoints=16;
        //total features=number of points*2 (because each Point has two coordenates) +
        // number of turning angles=2*numberOfPoints+2*(numberOfPoints-1)=4*numberOfPoints-2+1 (for the number of strokes)
        double[] features=new double[numberOfPoints*4-1];
        
        Symbol s=normalizeSymbol(symbol);


        //HERE ARE CONSIDERED JUST SYMBOLS WITH ONE STROKE
        Point[] p1=getNPoints(s , numberOfPoints);
        double[] pointsFeature=formatPoints(p1);



        // points to derive other features shouldnt be normalized
        //The here we extract N points from the original symbol
        Point[] p2=getNPoints(symbol , numberOfPoints);
        
        double[] tAnglesFeature=turningAngles(p2);

        int i=0;
        for (double d : pointsFeature) {
            features[i++]=d;
        }

        for (double d : tAnglesFeature) {
            features[i++]=d;
        }

        features[features.length-1]=symbol.getStrokes().size()/(double)maxNumberOfStrokes;
        return features;
    }

    public static Symbol normalizeSymbol(Symbol s){
        Symbol newS=new Symbol();
//        Point zeroPoint=s.getLtPoint();
//        double newX,newY;
//
//        double max=s.getHeight();
//        if(max<s.getWidth()){
//            max=s.getWidth();
//        }
//        for (Stroke str : s) {
//            Stroke newStroke=new Stroke();
//            for (Point p : str) {
//                Point newP=new Point();
//                newX=(p.getX()-zeroPoint.getX())/max;
//                newY=(p.getY()-zeroPoint.getY())/max;
//                newP.setLocation(newX, newY);
//                newP.setTimeInMiliseconds(p.getTimeInMiliseconds());
//                newStroke.addCheckingBoundingBox(newP);
//            }
//            newS.addCheckingBoundingBox(newStroke);
//        }
        return newS;
    }

}
