import java.time.Instant;

public class Sale {
    private int id;
    private Instant creationDatetime;
    private Order order;

    public Sale(){
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }
    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Sale{"
        + "id=" + id
        + ", creationDatetime=" + creationDatetime
        + ", order=" + order +
                '}';
    }
}
