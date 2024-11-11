package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoCompras {
    private static CarritoCompras instance;
    private Map<Producto, Integer> productos;

    private CarritoCompras() {
        productos = new HashMap<>();
    }

    public static CarritoCompras getInstance() {
        if (instance == null) {
            instance = new CarritoCompras();
        }
        return instance;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        productos.merge(producto, cantidad, Integer::sum);
    }

    public void removerProducto(Producto producto) {
    	System.out.print(producto.getNombre());
        productos.remove(producto);
    }

    public Map<Producto, Integer> getProductos() {
        return productos;
    }

    public void vaciarCarrito() {
        productos.clear();
    }

    public double getTotal() {
        return productos.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrecio() * entry.getValue())
                .sum();
    }
    public String getTotalFormateado() {
        return String.format("€%.2f", getTotal());
    }
}