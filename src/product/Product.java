package product;

public class Product {

	private int productId;
	private String pro_name;
	private int amount;
	private int category_id;
	private String created_at;
	private String updated_at;

	public Product() {

	}

	public Product(int productId, String pro_name, int category_id, int amount, String updated_at, String created_at) {
		this.productId = productId;
		this.pro_name = pro_name;
		this.category_id = category_id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.amount = amount;
	}

	public int amount() {
		return amount;
	}

	public String created_at() {
		return created_at;
	}

	public String updated_at() {
		return updated_at;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return pro_name;
	}

	public void setName(String name) {
		this.pro_name = name;
	}

	public int getCategory() {
		return category_id;
	}

	public void setCategory(int category) {
		this.category_id = category;
	}

	@Override
	public String toString() {
		return String.format("%10s%15s%15s%15s%25s%25s", productId, pro_name, amount, category_id, created_at,
				updated_at);

	}

}
