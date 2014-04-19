package br.usp.ime.escience.expressmatch.model;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "institution", catalog = "expressMatch")
public class Institution implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String acronym;
	private String nationality;
	private Set<UserInfo> userInfos = new HashSet<>(0);

	public Institution() {
	}

	public Institution(String name, String nationality) {
		this.name = name;
		this.nationality = nationality;
	}

	public Institution(String name, String acronym, String nationality,
			Set<UserInfo> userInfos) {
		this.name = name;
		this.acronym = acronym;
		this.nationality = nationality;
		this.userInfos = userInfos;
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

	@Column(name = "name", nullable = false, length = 256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "acronym", length = 45)
	public String getAcronym() {
		return this.acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	@Column(name = "nationality", nullable = false, length = 45)
	public String getNationality() {
		return this.nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "institution")
	public Set<UserInfo> getUserInfos() {
		return this.userInfos;
	}

	public void setUserInfos(Set<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acronym == null) ? 0 : acronym.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((nationality == null) ? 0 : nationality.hashCode());
		result = prime * result
				+ ((userInfos == null) ? 0 : userInfos.hashCode());
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
		if (!(obj instanceof Institution)) {
			return false;
		}
		Institution other = (Institution) obj;
		if (acronym == null) {
			if (other.acronym != null) {
				return false;
			}
		} else if (!acronym.equals(other.acronym)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (nationality == null) {
			if (other.nationality != null) {
				return false;
			}
		} else if (!nationality.equals(other.nationality)) {
			return false;
		}
		if (userInfos == null) {
			if (other.userInfos != null) {
				return false;
			}
		} else if (!userInfos.equals(other.userInfos)) {
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
		builder.append("Institution [id=").append(id).append(", name=")
				.append(name).append(", acronym=").append(acronym)
				.append(", nationality=").append(nationality).append("]");
		return builder.toString();
	}

}
