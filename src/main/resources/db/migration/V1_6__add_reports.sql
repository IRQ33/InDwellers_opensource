CREATE TABLE reports(
    id SERIAL PRIMARY KEY,
    reporting BIGINT,
    reported BIGINT,
    chat_id BIGINT,
    reason TEXT,
    approved BOOLEAN,
    admin BIGINT
)

/*
 object Reports : Table("reports") {
    val id = long("id").autoIncrement()
    val reporting = long("reporting")
    val reported = long("reported")
    val chatId = long("chat_id")
    val reason = text("reason").nullable()
    val approved = bool("approve").nullable()

    override val primaryKey = PrimaryKey(Reports.id)

}
 */