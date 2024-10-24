import { useEffect, useState } from 'react';
import axios from 'axios';
import BookPane from "./Book/BookPane.js"
import Navigation from './Navbar/NavBar.js';
import { Spinner, Row, Col } from 'reactstrap';
import Sidebar from './Sidebar/Sidebar.js';
import { TextField } from '@mui/material';

function Catalog (props) {
    const [books, setBooks] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);
    const [inputText, setInputText] = useState("")
    const [clickState, setClickState] = useState({});
    const axiosConfig = {
        headers: {
            "Content-Type": "application/json"
        }
    }
    let inputHandler = (e) => {
        let lowerCase = e.target.value.toLowerCase();
        setInputText(lowerCase);
      };

    const getBooks = () => {
        axios.get("http://localhost:3030/api/books").then((res) => {
            setBooks(res.data);

            const initialClickState = {};
            res.data.forEach(book => {
                initialClickState[book.isbn] = book.read === 1;
            });

            setClickState(initialClickState);

            setIsLoaded(true);

        })
    }
        

    useEffect( () => { getBooks() }, [])

    const setBookAsRead = (isbn, read) => {
        const currentRead = clickState[isbn] ? 1 : 0;

        const newRead = currentRead === 1 ? 0 : 1;

        setClickState(prevState => ({
            ...prevState,
            [isbn]: newRead === 1
        }));

        axios.post("http://localhost:3030/api/markRead", {isbn:isbn, read: newRead}, axiosConfig).then((res) => {
            console.log(res.data);
        }).catch((e) => {
            console.log(e)
        })
    }
   
    if(isLoaded){
        return (
            <>
                <Row>
                    <Col md="12" lg="12">
                        <Navigation />
                    </Col>
                </Row>
                <Row className='height-wrapper'>
                    <Col xl={3} className='filter-panel-wrapper'>
                        <Sidebar />
                    </Col>
                    <Col xl={9} className='book-wrapper'>
                        <div className='book-container'>
                            <TextField id="outlined-basic"
                            onChange={inputHandler}
                            variant="outlined"
                            fullWidth
                            label="Search book title..." />
                            <BookPane 
                                input={inputText} 
                                bookList={books} 
                                clickState={clickState}
                                setBookAsRead={setBookAsRead}/>
                        </div>
                    </Col>
                </Row>
            </>
        )
    }else{
        return <Spinner color="info">
        Loading...
      </Spinner>
    }
}
export default Catalog;