package sept.major.users.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.mindrot.jbcrypt.BCrypt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sept.major.common.entity.AbstractEntity;

@Getter
@Setter
@ToString
//@AllArgsConstructor //should not use the generated constructor since we need to hash the password for every setPassword
@NoArgsConstructor
//@EqualsAndHashCode  //Can't use the generated equal and hash functions due to hashing password
@Table(name = "users"/* , schema = "users" */)
@Entity
public class UserEntity implements AbstractEntity<String> {

    @Id
    @Setter(onMethod = @__(@Id))
    @NotBlank
    private String username;

	/**
	 * this attribute will save the hashed password and the plain text version is
	 * never saved
	 */
    @NotBlank
    private String password;

    /** TODO : change to Enum
     * types : 
     * C : Customer
     * S : Supervisor
     * W : Worker
     */
    @NotBlank
    @Column(name = "usertype")
    private String userType;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    @Override
    public String getID() {
        return username;
    }

	/**
	 * Hashes the plain text password before setting it.
	 * @param plainTextpassword
	 */
	public void setPassword(String plainTextPassword) {
		this.password = hashPassword(plainTextPassword);
	}

	/**
	 * for hashing password string using spring BCrypt library
	 * @param plainTextPassword
	 * @return hashed string
	 */
	public static String hashPassword(String plainTextPassword){
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}

	
	public boolean checkPassword(String plainTextPassword){
		return BCrypt.checkpw(plainTextPassword, this.password);
	}

	public UserEntity(@NotBlank String username, @NotBlank String password, @NotBlank String userType,
			@NotBlank String name, @NotBlank String phone, @NotBlank String address) {
		super();
		this.username = username;
		setPassword (password);
		this.userType = userType;
		this.name = name;
		this.phone = phone;
		this.address = address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
}
