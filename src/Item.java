public class Item {
    private String name;
    public String getname(){
        return this.name;
    }
    public void setname(String name){
        if (name!= null && name.length() >0) {
            this.name=String.valueOf(name);
        }
    }
    private String description;
    public String getdescription(){
        return this.description;
    }
    public void setdescription(String description){
        if (description!= null && description.length() >0) {
            this.description=String.valueOf(description);
        }
    }
    private Float old_price;
    public Float getold_price(){
        return this.old_price;
    }
    public void setold_price(String old_price){
        if (old_price!= null && old_price.length() >0) {
            this.old_price=Float.valueOf(removeAllNonDigits(old_price.replaceAll(",",".")));
        }
    }
    private Float new_price;
    public Float getnew_price(){
        return this.new_price;
    }
    public void setnew_price(String new_price){
        if (new_price!= null && new_price.length() >0) {
            this.new_price=Float.valueOf(removeAllNonDigits(new_price.replaceAll(",",".")));
        }
    }
    private static String removeAllNonDigits(String value) {
        String result = "";
        char ch;
        for (int i = 0; i < value.length(); i++) {
            ch = value.charAt(i);
            if (ch >= '0' && ch <= '9' || ch == '.' || ch == ',') {
                result += ch;
            }
        }
        return result;
    }
}
