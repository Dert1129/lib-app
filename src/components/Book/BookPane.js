import { CardBody, CardSubtitle, CardText, CardTitle, Row, Card, Col,  } from "reactstrap";

function BookPane({book}) {
    return(
        <div className="book shadow-sm">
            <img classNam="book-thumb" src="https://picsum.photos/300/200"></img>
            <Row className="content-wrapper">
                <Col xs={12} className="pb-1"><b>{book.title}</b></Col>
                <Col xs={12} className="pb-1">{book.authorName}</Col>
                <Col xs={12} className="pb-1">{book.isbn}</Col>
            </Row>
        </div>
    )
    
}

export default BookPane;