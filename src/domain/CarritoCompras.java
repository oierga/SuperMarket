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
    public double getTotal2() {
        double total = 0.0;
        for (Map.Entry<Producto, Integer> entry : productos.entrySet()) {
            Producto producto = entry.getKey();
            int cantidad = entry.getValue();
            total += producto.getPrecio() * cantidad;
        }
        return total;
    }


    public double getTotal() {
        return productos.entrySet().stream()
            .mapToDouble(e -> e.getKey().getPrecio() * e.getValue())
            .sum();
    }
    public String getTotalFormateado() {
        return String.format("€%.2f", getTotal());
    }
    public void mostrarCarrito() {
    	 for (Map.Entry<Producto, Integer> entry : productos.entrySet()) {
             Producto producto = entry.getKey();
             System.out.print(producto.getNombre()+": "+producto.getPrecio());
         }
    }
}
