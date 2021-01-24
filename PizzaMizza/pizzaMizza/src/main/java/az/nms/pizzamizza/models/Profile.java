package az.nms.pizzamizza.models;


public class Profile {

	public int id;
	public String name, email, city, postcode, pass;
	public int news;

	public Profile() {
		// TODO Auto-generated constructor stub
	}

	public Profile(String name, String email, String city, String postcode,
			String pass, int news) {
		super();
		this.name = name;
		this.email = email;
		this.city = city;
		this.postcode = postcode;
		this.pass = pass;
		this.news = news;
	}

	public int getNews() {
		return news;
	}

	public void setNews(int news) {
		this.news = news;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Profile [id=" + id + ", name=" + name + ", email=" + email
				+ ", city=" + city + ", postcode=" + postcode +  " pass=" + pass + " news=" + news + "]";
	}

	public String getCity() {
		return city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

}
