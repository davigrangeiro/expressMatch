package br.usp.ime.escience.expressmatch.model;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "symbol", catalog = "expressMatch")
public class Symbol implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Expression expression;
	private String label;
	private String href;
	private boolean representant;
	private Set<ShapeDescriptor> shapeDescriptors = new HashSet<>(0);
	private List<Stroke> strokes = new ArrayList<>(0);
	
	
	private Point ltPoint;
	private Point rbPoint;

	public Symbol() {
	}

	public Symbol(Expression expression) {
		this.expression = expression;
	}

	public Symbol(Expression expression, String label, List<Stroke> strokes) {
		this.expression = expression;
		this.label = label;
		this.strokes = strokes;
	}
	

    public boolean addCheckingBoundingBox(Stroke e) {
        Point p1= e.getLtPoint();
        Point p2= e.getRbPoint();
        if(this.getStrokes().isEmpty()){
            ltPoint = new Point(p1);
            rbPoint = new Point(p2);
        }
        else{
            if(p1.getX()<ltPoint.getX())
                ltPoint = new Point(p1.getX(),ltPoint.getY());
            if(p1.getY()<ltPoint.getY())
                ltPoint = new Point(ltPoint.getX(),p1.getY());

            if(p2.getX()>rbPoint.getX())
                rbPoint = new Point(p2.getX(),rbPoint.getY());
            if(p2.getY()>rbPoint.getY())
                rbPoint = new Point(rbPoint.getX(),p2.getY());
        }
        return this.getStrokes().add(e);
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
	@JoinColumn(name = "expression_id", nullable = false)
	public Expression getExpression() {
		return this.expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	@Column(name = "label", length = 40)
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	@Column(name = "representant", nullable = false)
	public boolean isRepresentant() {
		return this.representant;
	}

	public void setRepresentant(boolean representant) {
		this.representant = representant;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "symbol")
	public Set<ShapeDescriptor> getShapeDescriptors() {
		return this.shapeDescriptors;
	}

	public void setShapeDescriptors(Set<ShapeDescriptor> shapeDescriptors) {
		this.shapeDescriptors = shapeDescriptors;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "symbol", cascade={CascadeType.PERSIST})
	public List<Stroke> getStrokes() {
		return this.strokes;
	}

	public void setStrokes(List<Stroke> strokes) {
		this.strokes = strokes;
	}

	/**
	 * @return the href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * @param href the href to set
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((href == null) ? 0 : href.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		if (!(obj instanceof Symbol)) {
			return false;
		}
		Symbol other = (Symbol) obj;
		if (href == null) {
			if (other.href != null) {
				return false;
			}
		} else if (!href.equals(other.href)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (label == null) {
			if (other.label != null) {
				return false;
			}
		} else if (!label.equals(other.label)) {
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
		builder.append("Symbol [id=").append(id).append(", expression=")
				.append(expression).append(", label=").append(label)
				.append(", href=").append(href).append("]");
		return builder.toString();
	}

	/**
	 * @return the ltPoint
	 */
	@Transient
	public Point getLtPoint() {
		return ltPoint;
	}

	/**
	 * @param ltPoint the ltPoint to set
	 */
	public void setLtPoint(Point ltPoint) {
		this.ltPoint = ltPoint;
	}

	/**
	 * @return the rbPoint
	 */
	@Transient
	public Point getRbPoint() {
		return rbPoint;
	}

	/**
	 * @param rbPoint the rbPoint to set
	 */
	public void setRbPoint(Point rbPoint) {
		this.rbPoint = rbPoint;
	}

	
}
