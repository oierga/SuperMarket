package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.ServicioPersistenciaBD;

public class CarritoCompras {
    private static CarritoCompras instance;
    private HashMap<Producto, Integer> productos = new HashMap<>();

    public CarritoCompras() {}

    public static CarritoCompras getInstance() {
        if (instance == null) {
            instance = new CarritoCompras();
        }
        return instance;
    }

    public void agregarProducto(Producto producto, int cantidad) {
    	Map<String,Double> productosConDescuento = ServicioPersistenciaBD.getInstance().obtenerNombresConDescuento();
    	if (productosConDescuento.containsKey(producto.getNombre().toLowerCase())) {
    		producto.setPrecio(producto.getPrecio()*(1-productosConDescuento.get(producto.getNombre())*0.01));

    	}
    	productos.put(producto, productos.getOrDefault(producto, 0) + cantidad);
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
        return String.format("€%.2f", calcularTotalRecursivo());
    }

    public void mostrarCarrito() {
    	 for (Map.Entry<Producto, Integer> entry : productos.entrySet()) {
             Producto producto = entry.getKey();
             System.out.print(producto.getNombre()+": "+producto.getPrecio());
         }
    }
    
    public double calcularTotalRecursivo() {
        return calcularTotal(new ArrayList<>(productos.entrySet()), 0);
    }

    private double calcularTotal(List<Map.Entry<Producto, Integer>> items, int index) {
        if (index >= items.size()) {
            return 0;
        }
        Map.Entry<Producto, Integer> item = items.get(index);
        double subtotal = item.getKey().getPrecio() * item.getValue(); 
        return subtotal + calcularTotal(items, index + 1); 
    }
    
    //recursividad para contra cantidad total de productos
    public int contarProductos(List<Map.Entry<Producto, Integer>> items, int index) {
        if (index >= items.size()) {
            return 0; 
        }
        int cantidad = items.get(index).getValue(); 
        return cantidad + contarProductos(items, index + 1); 
    }

    //metodo pa facilitar el acceso
    public int contarProductosTotales() {
        return contarProductos(new ArrayList<>(productos.entrySet()), 0);
    }


}
