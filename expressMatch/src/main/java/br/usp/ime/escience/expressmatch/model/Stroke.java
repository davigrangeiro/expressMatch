package br.usp.ime.escience.expressmatch.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "stroke", catalog = "expressMatch")
public class Stroke implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Symbol symbol;
	private Integer strokeId;
	private List<Point> points = new ArrayList<>(0);
	
	
	private Point ltPoint;
	private Point rbPoint;

	public Stroke() {
	}
	
	public Stroke(Stroke s) {
		super();
		this.id = s.id;
		this.strokeId = s.strokeId;
		this.points = new ArrayList<Point>();
		
		for (Point p : s.points) {
			this.points.add(new Point(p));
		}
	}
	
	@Transient
	public Point getRepresentantPointOfStroke(){
		Point res = new Point();
		
		if (null == ltPoint || null == ltPoint){
			
			for (int i = 0; i < this.points.size(); i++) {
				this.addCheckingBoundingBox(this.points.remove(0));
			}
		}
		
		res.setX(this.ltPoint.getX() + (Math.abs(this.ltPoint.getX() + this.rbPoint.getX())/2));
		res.setX(this.rbPoint.getY() + (Math.abs(this.ltPoint.getY() + this.rbPoint.getX())/2));
		
		return res;
	}

	public Stroke(Symbol symbol) {
		this.symbol = symbol;
	}

	public Stroke(Symbol symbol, Point ltPoint, Point rbPoint, List<Point> points) {
		this.symbol = symbol;
		this.ltPoint = ltPoint;
		this.rbPoint = rbPoint;
		this.points = points;
	}
	
	@Transient
    public boolean addCheckingBoundingBox(Point e) {
        if(this.getPoints().isEmpty() || null == ltPoint || null == rbPoint){
            //The first Point2D added will be
            //the left-top and the right-bottom Point2D
            ltPoint = new Point(e);
            rbPoint = new Point(e);
        }
        else{
            if(e.getX()<ltPoint.getX())
                ltPoint = new Point(e.getX(),ltPoint.getY());
            if(e.getY()<ltPoint.getY())
                ltPoint = new Point(ltPoint.getX(),e.getY());

            if(e.getX()>rbPoint.getX())
                rbPoint = new Point(e.getX(),rbPoint.getY());
            if(e.getY()>rbPoint.getY())
                rbPoint = new Point(rbPoint.getX(),e.getY());
         }
        return this.getPoints().add(e);
    }

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "symbol_id", nullable = false)
	public Symbol getSymbol() {
		return this.symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	@Transient
	public Point getLtPoint() {
		return this.ltPoint;
	}

	public void setLtPoint(Point ltPoint) {
		this.ltPoint = ltPoint;
	}
	
	@Transient
	public Point getRbPoint() {
		return this.rbPoint;
	}

	public void setRbPoint(Point rbPoint) {
		this.rbPoint = rbPoint;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "stroke", cascade=CascadeType.ALL)
	public List<Point> getPoints() {
		return this.points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

	/**
	 * @return the strokeId
	 */
	@Column(name="stroke_id", nullable = false)
	public Integer getStrokeId() {
		return strokeId;
	}

	/**
	 * @param strokeId the strokeId to set
	 */
	public void setStrokeId(Integer strokeId) {
		this.strokeId = strokeId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ltPoint == null) ? 0 : ltPoint.hashCode());
		result = prime * result + ((points == null) ? 0 : points.hashCode());
		result = prime * result + ((rbPoint == null) ? 0 : rbPoint.hashCode());
		result = prime * result
				+ ((strokeId == null) ? 0 : strokeId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Stroke)) {
			return false;
		}
		Stroke other = (Stroke) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (ltPoint == null) {
			if (other.ltPoint != null) {
				return false;
			}
		} else if (!ltPoint.equals(other.ltPoint)) {
			return false;
		}
		if (points == null) {
			if (other.points != null) {
				return false;
			}
		} else if (!points.equals(other.points)) {
			return false;
		}
		if (rbPoint == null) {
			if (other.rbPoint != null) {
				return false;
			}
		} else if (!rbPoint.equals(other.rbPoint)) {
			return false;
		}
		if (strokeId == null) {
			if (other.strokeId != null) {
				return false;
			}
		} else if (!strokeId.equals(other.strokeId)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Stroke [id=").append(id).append(", symbol=")
				.append(symbol).append(", ltPoint=").append(ltPoint)
				.append(", rbPoint=").append(rbPoint).append(", strokeId=")
				.append(strokeId).append("]");
		return builder.toString();
	}

	
}
