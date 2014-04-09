package br.usp.ime.escience.model;

import java.io.Serializable;

/**
 * The Class Point.
 */
public class Point implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2687849602770343873L;

	/** The x and' y. */
	public Double x, y;

	/**
	 * Instantiates a new point.
	 */
	public Point() {
		super();
	}

	/**
	 * Instantiates a new point.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Point(Double x, Double y) {
		super();
		this.x = x;
		this.y = y;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
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
		if (!(obj instanceof Point)) {
			return false;
		}
		Point other = (Point) obj;
		if (x == null) {
			if (other.x != null) {
				return false;
			}
		} else if (!x.equals(other.x)) {
			return false;
		}
		if (y == null) {
			if (other.y != null) {
				return false;
			}
		} else if (!y.equals(other.y)) {
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
		builder.append("Point [");
		if (x != null) {
			builder.append("x=");
			builder.append(x);
			builder.append(", ");
		}
		if (y != null) {
			builder.append("y=");
			builder.append(y);
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public Double getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x the x to set
	 */
	public void setX(Double x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public Double getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y the y to set
	 */
	public void setY(Double y) {
		this.y = y;
	}
	
	
	
}
