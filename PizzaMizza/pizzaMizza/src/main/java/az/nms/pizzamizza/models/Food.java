package az.nms.pizzamizza.models;

import java.util.LinkedList;
import java.util.List;

public class Food {

	private int id;
	private double priceSmall, priceMedium, priceLarge;
	private String type, image, name;

   private List<Top> tops;

	public Food(){
	}

	public Food(int id, String image, String name, double priceSmall, double priceMedium, double priceLarge, String type){
		this.id = id;
		this.name = name;
		this.priceSmall = priceSmall;
		this.priceMedium = priceMedium;
		this.priceLarge = priceLarge;
		this.type = type;
		this.image = image;
		this.tops = new LinkedList<>();
	}

	public List<Top> getTops() {
		return tops;
	}

	public void setTops(List<Top> tops) {
		this.tops = tops;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPriceSmall() {
		return priceSmall;
	}

	public void setPriceSmall(double priceSmall) {
		this.priceSmall = priceSmall;
	}

	public double getPriceMedium() {
		return priceMedium;
	}

	public void setPriceMedium(double priceMedium) {
		this.priceMedium = priceMedium;
	}

	public double getPriceLarge() {
		return priceLarge;
	}

	public void setPriceLarge(double priceLarge) {
		this.priceLarge = priceLarge;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice(int size){
		switch (size){
			case 0: return priceSmall;
			case 1: return priceMedium;
			case 2: return priceLarge;
		}
		return priceMedium;
	}

	/*
	public String topsToString() {
		String rslt = "";
		for (int i = 0; i < tops.size(); i++) {
			if (i != tops.size() - 1)
				rslt += tops.get(i).getName() + ", ";
			else
				rslt += tops.get(i).getName();
		}
		return rslt;
	}

*/

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub

		if (o == null || o == this || !(o instanceof Food))
			return false;

		Food otherFood = (Food) o;

		if (otherFood.getId() != this.id)
			return false;
		if (!otherFood.getTops().equals(this.tops))
			return false;

		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}


}
