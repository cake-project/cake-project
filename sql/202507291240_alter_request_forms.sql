ALTER TABLE request_forms
    ADD CONSTRAINT fk_request_forms_proposal_form FOREIGN KEY (proposal_form_id) REFERENCES proposal_forms(id);