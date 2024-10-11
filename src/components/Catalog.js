import { useState } from 'react';
import logo from '../logo.svg';
import axios from 'axios';
import BookPane from "./Book/BookPane.js"
function Catalog (props) {

    const [books, setBooks] = useState([]);
    const [isLoaded, setIsLoaded] = useState(false);
    axios.get("http://localhost:3030/api/books").then((res) => {
        setBooks(res.data);
        setIsLoaded(true);
    })
    let bookPanel = [];
    bookPanel = books.map((element, index) => {
        console.log(books[index]);
        return <BookPane book={books[index]}/>
    })
    console.log(books);
    return bookPanel;
    if(isLoaded){
        return bookPanel;
    }else{
        return null;
    }
        // <BookPane books={books} isLoaded={isLoaded}/>
        
    //     <div>
    //         <header className="App-header">
    //     <img src={logo} className="App-logo" alt="logo" />
    //     <p>
    //       Edit <code>src/App.js</code> and save to reload.
    //     </p>
    //     <a
    //       className="App-link"
    //       href="https://reactjs.org"
    //       target="_blank"
    //       rel="noopener noreferrer"
    //     >
    //       Learn React
    //     </a>
    //   </header>
    //     </div>
}
export default Catalog;