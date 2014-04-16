package br.usp.ime.escience.expressmatch.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "user", catalog = "expressMatch", uniqueConstraints = @UniqueConstraint(columnNames = "nick"))
public class User implements java.io.Serializable {


	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nick;
	private String pass;
	private boolean enabled;
	private Date insertDate;
	private UserInfo userInfo;
	private Set<Authorities> authoritieses = new HashSet<>(0);

	public User() {
	}

	public User(String nick, String pass, boolean enabled, Date insertDate) {
		this.nick = nick;
		this.pass = pass;
		this.enabled = enabled;
		this.insertDate = insertDate;
	}

	public User(String nick, String pass, boolean enabled, Date insertDate,
			UserInfo userInfo, Set<Authorities> authoritieses) {
		this.nick = nick;
		this.pass = pass;
		this.enabled = enabled;
		this.insertDate = insertDate;
		this.userInfo = userInfo;
		this.authoritieses = authoritieses;
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

	@Column(name = "nick", unique = true, nullable = false, length = 50)
	public String getNick() {
		return this.nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@Column(name = "pass", nullable = false, length = 500)
	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Column(name = "enabled", nullable = false)
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "insert_date", nullable = false, length = 19)
	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
	public UserInfo getUserInfo() {
		return this.userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Authorities> getAuthoritieses() {
		return this.authoritieses;
	}

	public void setAuthoritieses(Set<Authorities> authoritieses) {
		this.authoritieses = authoritieses;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authoritieses == null) ? 0 : authoritieses.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((insertDate == null) ? 0 : insertDate.hashCode());
		result = prime * result + ((nick == null) ? 0 : nick.hashCode());
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
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
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (authoritieses == null) {
			if (other.authoritieses != null) {
				return false;
			}
		} else if (!authoritieses.equals(other.authoritieses)) {
			return false;
		}
		if (enabled != other.enabled) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (insertDate == null) {
			if (other.insertDate != null) {
				return false;
			}
		} else if (!insertDate.equals(other.insertDate)) {
			return false;
		}
		if (nick == null) {
			if (other.nick != null) {
				return false;
			}
		} else if (!nick.equals(other.nick)) {
			return false;
		}
		if (pass == null) {
			if (other.pass != null) {
				return false;
			}
		} else if (!pass.equals(other.pass)) {
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
