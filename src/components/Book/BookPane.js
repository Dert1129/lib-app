import {Row, Col } from "reactstrap";
import { faPenToSquare, faBookmark } from "@fortawesome/free-solid-svg-icons";
import { faBookmark as faRegularBookmark } from "@fortawesome/free-regular-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { useState } from "react";
import { EditModal } from "../EditModal/EditModal";

function BookPane({clickState, setBookAsRead, bookList, input}) {
    const [modalStates, setModalStates] = useState({});
    const filteredData = bookList.filter((el) => {
        if (input === ''){
            return el;
        }else {
            return el.title.toLowerCase().includes(input)
        }
    })

    const handleEditClick = (isbn) => {
        setModalStates((prevStates) => ({
          ...prevStates,
          [isbn]: !prevStates[isbn],
        }));
      };
    
    return(
        <>
        {filteredData.map((item) => (
            <div className="book shadow-sm">
                <div className="book-main-content">
                    {!clickState[item.isbn] ? 

                    <div id="mark-read-button" onClick={() => setBookAsRead(item.isbn)}><FontAwesomeIcon icon={faRegularBookmark} /><span className="read-text">Mark read</span></div>
                    : 
                    <div id="mark-read-button" onClick={() => setBookAsRead(item.isbn)}><FontAwesomeIcon icon={faBookmark} /><span className="read-text">Marked read</span></div>}

                    <img className="book-thumb shadow-sm" src={item.imageLink} alt={item.title}></img>
                    
                    <Row className="content-wrapper">
                        <div className="edit-icon-wrapper" onClick={() => handleEditClick(item.isbn)}>
                            <FontAwesomeIcon icon={faPenToSquare} />
                        </div>
                        <Col xs={12} className="pb-1 book-title"><b>{item.title}</b></Col>
                        <Col xs={12} className="pb-1 text-secondary">{item.authorName}</Col>
                        {item.genre ? <Col xs={12} className="pb-1 text-secondary">Genre:{item.genre}</Col> : null}
                        <Col xs={12} className="pb-1 text-secondary">ISBN: {item.isbn}</Col>
                    </Row>
                </div>
                <EditModal close={() => handleEditClick(item.isbn)}  show={modalStates[item.isbn] || false} isbn={item.isbn} genre={item.genre} title={item.title} authorName={item.authorName}/>
            </div>
            
        ))
        }
    </>
    )
}

export default BookPane;