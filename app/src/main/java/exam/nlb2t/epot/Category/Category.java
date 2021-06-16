package exam.nlb2t.epot.Category;

public class Category {
    private String id;
    private String name;
    private int avatar_id;

    public Category(String id, String name, int avatar_id) {
        this.id = id;
        this.name = name;
        this.avatar_id = avatar_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatar_id() {
        return avatar_id;
    }

    public void setAvatar_id(int avatar_id) {
        this.avatar_id = avatar_id;
    }
}
