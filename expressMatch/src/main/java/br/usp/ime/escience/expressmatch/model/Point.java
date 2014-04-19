package br.usp.ime.escience.expressmatch.model;


import java.awt.geom.Point2D;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "point", catalog = "expressMatch")
public class Point implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Stroke stroke;
	private float x;
	private float y;
	private Date time;

	public Point() {
	}

	public Point(Stroke stroke, float x, float y) {
		this.stroke = stroke;
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p) {
		super();
		this.x = p.x;
		this.y = p.y;
	}

	public Point(Stroke stroke, float x, float y, Date time) {
		this.stroke = stroke;
		this.x = x;
		this.y = y;
		this.time = time;
	}

	public Point(float x, float y) {
		super();
		this.x = x;
		this.y = y;
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
	@JoinColumn(name = "id_stroke", nullable = false)
	public Stroke getStroke() {
		return this.stroke;
	}

	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}

	@Column(name = "x", nullable = false, precision = 12, scale = 0)
	public float getX() {
		return this.x;
	}

	public void setX(float x) {
		this.x = x;
	}

	@Column(name = "y", nullable = false, precision = 12, scale = 0)
	public float getY() {
		return this.y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time", length = 19)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Point [id=").append(id).append(", stroke=")
				.append(stroke).append(", x=").append(x).append(", y=")
				.append(y).append(", time=").append(time).append("]");
		return builder.toString();
	}
	
	@Transient
	public double distance(Point p){
		return Point2D.distance(this.x, this.y, p.x, p.y);
	}
	
	public void setLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}
}