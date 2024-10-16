import React, { useState } from 'react';
import {
  Collapse,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Nav,
  NavItem,
  NavLink,
} from 'reactstrap';

function Navigation(props) {
  const [collapsed, setCollapsed] = useState(true);

  const toggleNavbar = () => setCollapsed(!collapsed);

  return (
    <div>
      <Navbar color="faded" light>
        <NavbarBrand href="/" className="me-auto">
          Liv's Library
        </NavbarBrand>
          <Nav navbar>
            <NavItem>
              <NavLink style={{display: 'flex'}} href="/components/">Upload a book</NavLink>
            </NavItem>
          </Nav>
      </Navbar>
    </div>
  );
}

export default Navigation;