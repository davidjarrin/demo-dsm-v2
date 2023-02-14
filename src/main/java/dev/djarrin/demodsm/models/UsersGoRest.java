package dev.djarrin.demodsm.models;

public class UsersGoRest {
    
    private String id;
    private String name;
    private String email;
    private String gender;
    private String status;
    private double saldo;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public double getSaldo() {
        return saldo;
    }
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    @Override
    public String toString() {
        return "UsersGoRest [id=" + id + ", name=" + name + ", email=" + email + ", gender=" + gender + ", status="
                + status + ", saldo=" + saldo + "]";
    }
    
    

    
}
