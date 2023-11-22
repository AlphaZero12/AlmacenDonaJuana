package com.example.almacen_doajuana.Modal;

public class Productos {
    private String nombre,descripcion,precioven,categoria,imagen,fecha,hora,pid,cantidad,preciocom;

    public Productos() {}

    public Productos(String nombre, String descripcion, String precioven, String categoria, String imagen, String fecha, String hora, String pid, String cantidad, String preciocom) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioven = precioven;
        this.categoria = categoria;
        this.imagen = imagen;
        this.fecha = fecha;
        this.hora = hora;
        this.pid = pid;
        this.cantidad = cantidad;
        this.preciocom = preciocom;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecioven() {
        return precioven;
    }

    public void setPrecioven(String precioven) {
        this.precioven = precioven;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPreciocom() {
        return preciocom;
    }

    public void setPreciocom(String preciocom) {
        this.preciocom = preciocom;
    }
}

