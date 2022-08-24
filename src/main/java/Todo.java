class Todo extends Task {
    private static int typeKey = Task.getTypeKey(Task.Type.TODO);

    private static String extractName(String input) throws CarbonException {
        int len = input.length();
        int requiredLen = "todo ".length();
        if (len <= requiredLen) {
            CarbonException invalidParam = new InvalidParamException(input);
            throw invalidParam;
        } else {
            String name = input.substring("todo ".length());
            return name;
        }
    }

    public static Task createTask(String input) {
        String name = Todo.extractName(input);
        Task todo = new Todo(name, false);
        return todo;
    }

    public static Task loadTask(String name, Boolean isDone) {
        Task todo = new Todo(name, isDone);
        return todo;
    }

    private Todo(String name, Boolean isDone) {
        super(name, isDone);
    }

    @Override
    public String encode() {
        int typeKey = Todo.typeKey;
        int isDone = this.isDone ? 1 : 0;
        String result = String.format("%d|%d|%s\n", typeKey, isDone, this.name); 
        return result;
    }

    @Override
    public String toString() {
        String type = "\u001B[35m(TODO)\u001B[0m";
        return String.format("%s %s !", type, super.toString());
    }
}
