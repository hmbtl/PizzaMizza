package az.nms.pizzamizza.models;

import az.nms.pizzamizza.SharedData;

public class Top {

	private int id,editable;
	private String nameAz, nameEn, nameRu;

	private int size;
	public Top() {
	}

	public Top(int id, String nameAz, String nameEn, String nameRu,int editable){
		this.id = id;
		this.nameAz = nameAz;
		this.nameEn = nameEn;
		this.nameRu = nameRu;
		this.editable = editable;
		this.size = 0;
	}


	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEditable() {
		return editable;
	}

	public void setEditable(int editable) {
		this.editable = editable;
	}

	public String getName() {
		if(SharedData.getLanguage().equals("az"))
			return nameAz;
		else if(SharedData.getLanguage().equals("en"))
			return nameEn;
		else if(SharedData.getLanguage().equals("ru"))
			return nameRu;
		else return nameAz;
	}


	public String getNameAz() {
		return nameAz;
	}

	public void setNameAz(String nameAz) {
		this.nameAz = nameAz;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameRu() {
		return nameRu;
	}

	public void setNameRu(String nameRu) {
		this.nameRu = nameRu;
	}




	public String toString() {
		return this.getName();
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o == null || o == this || !(o instanceof Top))
			return false;

		Top otherTop = (Top) o;

		if (otherTop.getId() != this.id)
			return false;
		if (otherTop.getSize() != this.size)
			return false;

		return true;
	}


}
