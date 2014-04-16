package br.usp.ime.escience.expressmatch.model;



import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "user_info", catalog = "expressMatch")
public class UserInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private User user;
	private Institution institution;
	private String name;
	private String nationaity;
	private Set<Expression> expressions = new HashSet<>(0);

	public UserInfo() {
	}

	public UserInfo(User user, Institution institution, String name) {
		this.user = user;
		this.institution = institution;
		this.name = name;
	}

	public UserInfo(User user, Institution institution, String name,
			String nationaity, Set<Expression> expressions) {
		this.user = user;
		this.institution = institution;
		this.name = name;
		this.nationaity = nationaity;
		this.expressions = expressions;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instituition_id", nullable = false)
	public Institution getInstitution() {
		return this.institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	@Column(name = "name", nullable = false, length = 256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "nationaity", length = 45)
	public String getNationaity() {
		return this.nationaity;
	}

	public void setNationaity(String nationaity) {
		this.nationaity = nationaity;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userInfo")
	public Set<Expression> getExpressions() {
		return this.expressions;
	}

	public void setExpressions(Set<Expression> expressions) {
		this.expressions = expressions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expressions == null) ? 0 : expressions.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((institution == null) ? 0 : institution.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((nationaity == null) ? 0 : nationaity.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		if (!(obj instanceof UserInfo)) {
			return false;
		}
		UserInfo other = (UserInfo) obj;
		if (expressions == null) {
			if (other.expressions != null) {
				return false;
			}
		} else if (!expressions.equals(other.expressions)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (institution == null) {
			if (other.institution != null) {
				return false;
			}
		} else if (!institution.equals(other.institution)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (nationaity == null) {
			if (other.nationaity != null) {
				return false;
			}
		} else if (!nationaity.equals(other.nationaity)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}

}
