package net.newglobe.app.model;

import java.util.List;

import javax.persistence.Transient;

import net.newglobe.app.model.vo.BaseModel;

public class SysAccount extends BaseModel {
	
	protected static final long serialVersionUID = 5172776809901424891L;

	private String username;

    private String password;

    private String salt;

    private String loginIp;

	@Transient
    private Long pilePartition;
    
    private String phone;
    
    @Transient
    private boolean isRoot = false;//是否为超级管理员
    @Transient//瞬时的，不作为数据库字段
    private List<SysPower> powers;//所有权限
    @Transient//瞬时的，不作为数据库字段
    private List<SysRole> roles;//菜单权限
    @Transient
    private String pilePartitionName;//所属区域名称
    @Transient
    private String roleName;//角色名称
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

	public List<SysPower> getPowers() {
		return powers;
	}

	public void setPowers(List<SysPower> powers) {
		this.powers = powers;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public Long getPilePartition() {
		return pilePartition;
	}

	public void setPilePartition(Long pilePartition) {
		this.pilePartition = pilePartition;
	}

	public String getPilePartitionName() {
		return pilePartitionName;
	}

	public void setPilePartitionName(String pilePartitionName) {
		this.pilePartitionName = pilePartitionName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}