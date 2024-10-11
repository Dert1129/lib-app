import { CardBody, CardSubtitle, CardText, CardTitle, Col, Row, Card } from "reactstrap";

function BookPane({book}) {
    console.log(book)
    return(
        <Row>
            <Card outline style={{width: "18rem"}} className="my-2">
                <img src="https://picsum.photos/300/200"></img>
                <CardBody>
                    <CardTitle tag={"h5"}>{book.title}</CardTitle>
                </CardBody>
                <CardSubtitle className="mb-2 text-muted">{book.authorName}</CardSubtitle>
                <CardText>Copies: {book.copies} ISBN: {book.isbn}</CardText>
            </Card>
        </Row>
    )
    
}
export default BookPane;