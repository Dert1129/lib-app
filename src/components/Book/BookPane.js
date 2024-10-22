import {Row, Col } from "reactstrap";
import { faPenToSquare, faBookmark } from "@fortawesome/free-solid-svg-icons";
import { faBookmark as faRegularBookmark } from "@fortawesome/free-regular-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

function BookPane({clickState, setBookAsRead, bookList, input}) {
    const filteredData = bookList.filter((el) => {
        if (input === ''){
            return el;
        }else {
            return el.title.toLowerCase().includes(input)
        }
    })
    
    return(
        <>
        {filteredData.map((item) => (
            <div className="book shadow-sm">
                {!clickState[item.isbn] ? 
                <FontAwesomeIcon id="mark-read-button" onClick={() => setBookAsRead(item.isbn, 0)} icon={faRegularBookmark} /> 
                : 
                <FontAwesomeIcon id="mark-read-button" onClick={() => setBookAsRead(item.isbn, 1)} icon={faBookmark} />} 
                <img className="book-thumb shadow-sm" src={item.imageLink} alt={item.title}></img>
                <Row className="content-wrapper">
                    <div className="edit-icon-wrapper">
                    <FontAwesomeIcon icon={faPenToSquare} />
                    </div>
                    <Col xs={12} className="pb-1"><b>{item.title}</b></Col>
                    <Col xs={12} className="pb-1">{item.authorName}</Col>
                    <Col xs={12} className="pb-1">ISBN: {item.isbn}</Col>
                    {item.genre ? <Col xs={12} className="pb-1">{item.genre}</Col> : null}
                </Row>
                
            </div>
            
        ))
        }
    </>
    )
}

export default BookPane;