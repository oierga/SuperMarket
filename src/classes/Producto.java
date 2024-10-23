package classes;


public class Producto {

	private String nombre;
	private String medida;
	private int peso;
	private int calorias;
	private int proteina;
	private int grasa;
	private int grasa_sat;
	private int fibra;
	private int carbohidratos;
	private String categoria;
	public Producto(String nombre, String medida, int peso, int calorias, int proteina, int grasa, int grasa_sat,
			int fibra, int carbohidratos, String categoria) {
		super();
		this.nombre = nombre;
		this.medida = medida;
		this.peso = peso;
		this.calorias = calorias;
		this.proteina = proteina;
		this.grasa = grasa;
		this.grasa_sat = grasa_sat;
		this.fibra = fibra;
		this.carbohidratos = carbohidratos;
		this.categoria = categoria;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getMedida() {
		return medida;
	}
	public void setMedida(String medida) {
		this.medida = medida;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	public int getCalorias() {
		return calorias;
	}
	public void setCalorias(int calorias) {
		this.calorias = calorias;
	}
	public int getProteina() {
		return proteina;
	}
	public void setProteina(int proteina) {
		this.proteina = proteina;
	}
	public int getGrasa() {
		return grasa;
	}
	public void setGrasa(int grasa) {
		this.grasa = grasa;
	}
	public int getGrasa_sat() {
		return grasa_sat;
	}
	public void setGrasa_sat(int grasa_sat) {
		this.grasa_sat = grasa_sat;
	}
	public int getFibra() {
		return fibra;
	}
	public void setFibra(int fibra) {
		this.fibra = fibra;
	}
	public int getCarbohidratos() {
		return carbohidratos;
	}
	public void setCarbohidratos(int carbohidratos) {
		this.carbohidratos = carbohidratos;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	@Override
	public String toString() {
		return "Producto [nombre=" + nombre + ", medida=" + medida + ", peso=" + peso + ", calorias=" + calorias
				+ ", proteina=" + proteina + ", grasa=" + grasa + ", grasa_sat=" + grasa_sat + ", fibra=" + fibra
				+ ", carbohidratos=" + carbohidratos + ", categoria=" + categoria + "]";
	}
	public static Producto parseCSV(String line) {
        String[] parts = line.split(",");
        String nombre = parts[0].replaceAll("\"", "");
        String medida = parts[1].replaceAll("\"", "");
        int peso = Integer.parseInt(parts[2].replaceAll("\"", ""));
        int calorias = Integer.parseInt(parts[3].replaceAll("\"", ""));
        int proteina = Integer.parseInt(parts[4].replaceAll("\"", ""));
        int grasa = Integer.parseInt(parts[5].replaceAll("\"", ""));
        int grasa_sat = Integer.parseInt(parts[6].replaceAll("\"", ""));
        int fibra = Integer.parseInt(parts[7].replaceAll("\"", ""));
        int carbohidratos = Integer.parseInt(parts[8].replaceAll("\"", ""));
        String categoria = parts[9].replaceAll("\"", "");
        return new Producto(nombre, medida, peso, calorias, proteina, grasa, grasa_sat, fibra, 
        		carbohidratos, categoria);
    }
}
