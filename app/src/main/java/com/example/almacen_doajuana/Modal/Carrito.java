package com.example.almacen_doajuana.Modal;

public class Carrito {
    private String precio,cantidad,descuento,nombre,pid;

    public Carrito(){

    }

    public Carrito(String precio, String cantidad, String descuento, String nombre, String pid) {
        this.precio = precio;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.nombre = nombre;
        this.pid = pid;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
