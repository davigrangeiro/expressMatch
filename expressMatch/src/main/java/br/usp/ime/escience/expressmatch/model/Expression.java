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
@Table(name = "expression", catalog = "expressMatch")
public class Expression implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private UserInfo userInfo;
	private ExpressionType expressionType;
	private String label;
	private Set<Symbol> symbols = new HashSet<>(0);
	private Set<ExpressionType> expressionTypes = new HashSet<>(0);

	public Expression() {
	}

	public Expression(UserInfo userInfo, ExpressionType expressionType,
			String label, Set<Symbol> symbols, Set<ExpressionType> expressionTypes) {
		this.userInfo = userInfo;
		this.expressionType = expressionType;
		this.label = label;
		this.symbols = symbols;
		this.expressionTypes = expressionTypes;
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
	@JoinColumn(name = "user_info_id")
	public UserInfo getUserInfo() {
		return this.userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "expression_type_id")
	public ExpressionType getExpressionType() {
		return this.expressionType;
	}

	public void setExpressionType(ExpressionType expressionType) {
		this.expressionType = expressionType;
	}

	@Column(name = "label", length = 1024)
	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "expression")
	public Set<Symbol> getSymbols() {
		return this.symbols;
	}

	public void setSymbols(Set<Symbol> symbols) {
		this.symbols = symbols;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "expression")
	public Set<ExpressionType> getExpressionTypes() {
		return this.expressionTypes;
	}

	public void setExpressionTypes(Set<ExpressionType> expressionTypes) {
		this.expressionTypes = expressionTypes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expressionType == null) ? 0 : expressionType.hashCode());
		result = prime * result
				+ ((expressionTypes == null) ? 0 : expressionTypes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((symbols == null) ? 0 : symbols.hashCode());
		result = prime * result
				+ ((userInfo == null) ? 0 : userInfo.hashCode());
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
		if (!(obj instanceof Expression)) {
			return false;
		}
		Expression other = (Expression) obj;
		if (expressionType == null) {
			if (other.expressionType != null) {
				return false;
			}
		} else if (!expressionType.equals(other.expressionType)) {
			return false;
		}
		if (expressionTypes == null) {
			if (other.expressionTypes != null) {
				return false;
			}
		} else if (!expressionTypes.equals(other.expressionTypes)) {
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
		if (symbols == null) {
			if (other.symbols != null) {
				return false;
			}
		} else if (!symbols.equals(other.symbols)) {
			return false;
		}
		if (userInfo == null) {
			if (other.userInfo != null) {
				return false;
			}
		} else if (!userInfo.equals(other.userInfo)) {
			return false;
		}
		return true;
	}

}
