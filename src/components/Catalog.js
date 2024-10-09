import logo from '../logo.svg';
function Catalog (props) {
    console.log(props)
    return(
        <div>
            <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <a href='/scan'>Scan a new book!</a>
      </header>
        </div>
    )
}
export default Catalog;