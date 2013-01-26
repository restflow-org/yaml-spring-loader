package org.restflow.foo;

public class FooAuthor {

	private String first;
	private String last;
	
	private FooAddress address;
	private FooPublisher publisher;
	
	public String getFirst() { return first; }
	public void setFirst(String first) {this.first = first;}
	public String getLast() {return last;}
	public void setLast(String last) { this.last = last; }
	public FooAddress getAddress() {return address;}
	public void setAddress(FooAddress address) { this.address = address;}
	public FooPublisher getPublisher() { return publisher; }
	public void setPublisher(FooPublisher publisher) { this.publisher = publisher;}
	
}
