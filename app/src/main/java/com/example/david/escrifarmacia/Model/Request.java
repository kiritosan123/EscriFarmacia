package com.example.david.escrifarmacia.Model;

import java.util.List;

public class Request {
    private String phone;
    private String name;
    private String adrress;
    private String total;
    private String status;
    private List<Order> medicaments; // Para lista de pedidos(solicitud) de medicamentos

    public Request() {
    }

    public Request(String phone, String name, String adrress, String total, List<Order> medicaments) {
        this.phone = phone;
        this.name = name;
        this.adrress = adrress;
        this.total = total;
        this.medicaments = medicaments;
        this.status = "0";

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdrress() {
        return adrress;
    }

    public void setAdrress(String adrress) {
        this.adrress = adrress;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(List<Order> medicaments) {
        this.medicaments = medicaments;
    }
}
