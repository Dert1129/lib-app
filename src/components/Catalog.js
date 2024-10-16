import { useEffect, useState } from 'react';
import axios from 'axios';
import BookPane from "./Book/BookPane.js"
import Navigation from './Navbar/NavBar.js';
import { Spinner, Row, Col } from 'reactstrap';
import Sidebar from './Sidebar/Sidebar.js';
function Catalog (props) {

    const [books, setBooks] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);
    const getBooks = () => {
        axios.get("http://localhost:3030/api/books").then((res) => {
            setBooks(res.data);
            setIsLoaded(true);
        })
    }

    useEffect(() => {
        getBooks();
    }, [])
    
    let bookPanel = [];
        bookPanel = books.map((element, index) => {
        console.log(books[index]);
        return (
            <BookPane book={books[index]}/>
        )
    })
    console.log(books);
    if(isLoaded){
        return (
            <>
                <Row>
                    <Col md="12" lg="12">
                        <Navigation />
                    </Col>
                </Row>
                <div className='height-wrapper'>
                    <Row>
                        <Col xl={3} className='filter-panel-wrapper'>
                            <Sidebar />
                        </Col>
                        <Col xl={9} className='book-wrapper'>
                            <div className='book-container'>
                                {bookPanel}
                            </div>
                        </Col>
                    </Row>
                </div>   
            </>
        )
    }else{
        return <Spinner color="info">
        Loading...
      </Spinner>
    }
}
export default Catalog;