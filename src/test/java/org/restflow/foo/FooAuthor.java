package ssrl.beans;

public class Person {

	private String first;
	private String last;
	
	private Address address;
	private Publisher publisher;
	
	public String getFirst() { return first; }
	public void setFirst(String first) {this.first = first;}
	public String getLast() {return last;}
	public void setLast(String last) { this.last = last; }
	public Address getAddress() {return address;}
	public void setAddress(Address address) { this.address = address;}
	public Publisher getPublisher() { return publisher; }
	public void setPublisher(Publisher publisher) { this.publisher = publisher;}
	
}
