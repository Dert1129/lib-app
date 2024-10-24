import { Modal, ModalHeader, ModalBody, Row, Input, Button } from "reactstrap";

export function EditModal({isbn, title, genre, authorName, show, close}) {

    return (
        <Modal isOpen={show}>
            <ModalHeader toggle={close}>
                {title}
            </ModalHeader>
            <ModalBody>
                <Row className="modal-item">
                    {authorName ? <span>Author: {authorName}</span> : <Input name="author-input" placeholder="Enter Author"/>}
                </Row>
                <Row className="modal-item">
                {genre ? <span>Genre: {genre}</span> : <Input className="input-text-box" name="genre-input" placeholder="Enter Genre" />}
                 </Row>
                
                <Row className="modal-item">
                    {isbn ? <span>ISBN: {isbn}</span> : <Input name="isbn-input" placeholder="Enter ISBN" />}
                </Row>
                <Button type="submit">Submit</Button>
            </ModalBody>
        </Modal>
    )
}