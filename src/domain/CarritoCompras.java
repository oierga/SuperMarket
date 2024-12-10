package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoCompras {
    private static CarritoCompras instance;
    private Map<Producto, Integer> productos = new HashMap<>();

    private CarritoCompras() {}

    public static CarritoCompras getInstance() {
        if (instance == null) {
            instance = new CarritoCompras();
        }
        return instance;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        productos.put(producto, productos.getOrDefault(producto,0) + cantidad);
    }

    public void removerProducto(Producto producto) {
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
            .mapToDouble(e -> e.getKey().getPrecio() * e.getValue())
            .sum();
    }
    public String getTotalFormateado() {
        return String.format("€%.2f", getTotal());
    }
}
