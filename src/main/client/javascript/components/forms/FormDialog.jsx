import { Button, Dialog, DialogActions, DialogContent, DialogTitle } from 'material-ui';
import PropTypes from 'prop-types';
import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { submit } from 'redux-form';

class FormDialog extends Component {
  static propTypes = {
    title: PropTypes.string.isRequired,
    formComponent: PropTypes.oneOfType([PropTypes.func, PropTypes.object]).isRequired,
    formName: PropTypes.string.isRequired,
    dispatch: PropTypes.func.isRequired,
    onCancel: PropTypes.func.isRequired,
    onSubmit: PropTypes.func.isRequired,
    onDelete: PropTypes.func,
    open: PropTypes.bool,
    cancelButtonText: PropTypes.string,
    submitButtonText: PropTypes.string,
    deleteButtonText: PropTypes.string,
    initialValues: PropTypes.object,
  };

  static defaultProps = {
    open: false,
    cancelButtonText: 'Cancel',
    submitButtonText: 'Submit',
    deleteButtonText: 'Delete',
    onDelete: null,
    initialValues: null,
  };

  handleSubmit = () => {
    const { dispatch, formName } = this.props;

    dispatch(submit(formName));
  };

  handleDelete = () => {
    const { onDelete, initialValues } = this.props;

    return onDelete && onDelete(initialValues);
  };

  render() {
    const {
      title,
      open,
      cancelButtonText,
      submitButtonText,
      deleteButtonText,
      onCancel,
      onSubmit,
      onDelete,
      formComponent: FormComponent,
      ...rest
    } = this.props;

    const actions = (
      <Fragment>
        <Button variant="flat" onTouchTap={onCancel}>
          {cancelButtonText}
        </Button>
        {onDelete && <Button variant="flat" secondary onTouchTap={this.handleDelete}>{deleteButtonText}</Button>}
        <Button
          variant="flat"
          color="primary"
          onTouchTap={this.handleSubmit}
        >
          {submitButtonText}
        </Button>
      </Fragment>);

    return (
      <Dialog title={title} open={open} onClose={onCancel} fullWidth maxWidth="sm">
        <DialogTitle>
          {title}
        </DialogTitle>
        <DialogContent>
          <FormComponent onSubmit={onSubmit} {...rest} />
        </DialogContent>
        <DialogActions>
          {actions}
        </DialogActions>
      </Dialog>
    );
  }
}

export default connect()(FormDialog);
