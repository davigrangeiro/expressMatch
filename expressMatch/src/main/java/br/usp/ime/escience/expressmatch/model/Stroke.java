package br.usp.ime.escience.expressmatch.model;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "stroke", catalog = "expressMatch")
public class Stroke implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Symbol symbol;
	private Float ltPoint;
	private Float rbPoint;
	private Integer strokeId;
	private Set<Point> points = new HashSet<>(0);

	public Stroke() {
	}

	public Stroke(Symbol symbol) {
		this.symbol = symbol;
	}

	public Stroke(Symbol symbol, Float ltPoint, Float rbPoint, Set<Point> points) {
		this.symbol = symbol;
		this.ltPoint = ltPoint;
		this.rbPoint = rbPoint;
		this.points = points;
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

	@Column(name = "lt_point", precision = 12, scale = 0)
	public Float getLtPoint() {
		return this.ltPoint;
	}

	public void setLtPoint(Float ltPoint) {
		this.ltPoint = ltPoint;
	}

	@Column(name = "rb_point", precision = 12, scale = 0)
	public Float getRbPoint() {
		return this.rbPoint;
	}

	public void setRbPoint(Float rbPoint) {
		this.rbPoint = rbPoint;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stroke")
	public Set<Point> getPoints() {
		return this.points;
	}

	public void setPoints(Set<Point> points) {
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
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
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
		if (symbol == null) {
			if (other.symbol != null) {
				return false;
			}
		} else if (!symbol.equals(other.symbol)) {
			return false;
		}
		return true;
	}
}
