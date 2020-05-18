package id.ac.umn.simplymeal.Location;

public class Address {
    String AddressLine;
    String Locality; //city
    String AdminArea; //state
    String CountryName;
    String postalCode;

    public Address(String addressLine, String locality, String adminArea, String countryName, String postalCode) {
        AddressLine = addressLine;
        Locality = locality;
        AdminArea = adminArea;
        CountryName = countryName;
        this.postalCode = postalCode;
    }

    public Address() {
    }

    public String getAddressLine() {
        return AddressLine;
    }

    public void setAddressLine(String addressLine) {
        AddressLine = addressLine;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getAdminArea() {
        return AdminArea;
    }

    public void setAdminArea(String adminArea) {
        AdminArea = adminArea;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
