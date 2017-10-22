public class Id {
    private TypeOfId type;
    private String name;

    public Id(TypeOfId type, String name) {
        this.type = type;
        this.name = name;
    }

    public Id(String name) {
        this.name = name;
    }

    public TypeOfId getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Id id = (Id) o;

        return name != null ? name.equals(id.name) : id.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
