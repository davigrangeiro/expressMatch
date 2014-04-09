package br.usp.ime.escience.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * The Class Stroke.
 */
public class Stroke implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3485162861580526875L;

	/** The id. */
	private Long id;
	
	/** The time. */
	private Date time;
	
	/** The points. */
	private Point[] points;
	
	/**
	 * Instantiates a new stroke.
	 */
	public Stroke() {
		super();
	}

	/**
	 * Instantiates a new stroke.
	 *
	 * @param id the id
	 * @param time the time
	 * @param points the points
	 */
	public Stroke(Long id, Date time, Point[] points) {
		super();
		this.id = id;
		this.time = time;
		this.points = points;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the time.
	 *
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * Sets the time.
	 *
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * Gets the points.
	 *
	 * @return the points
	 */
	public Point[] getPoints() {
		return points;
	}

	/**
	 * Sets the points.
	 *
	 * @param points the points to set
	 */
	public void setPoints(Point[] points) {
		this.points = points;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Arrays.hashCode(points);
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		if (!Arrays.equals(points, other.points)) {
			return false;
		}
		if (time == null) {
			if (other.time != null) {
				return false;
			}
		} else if (!time.equals(other.time)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("Stroke [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (time != null) {
			builder.append("time=");
			builder.append(time);
			builder.append(", ");
		}
		if (points != null) {
			builder.append("points=");
			builder.append(Arrays.asList(points).subList(0,
					Math.min(points.length, maxLen)));
		}
		builder.append("]");
		return builder.toString();
	}
	
	
}
