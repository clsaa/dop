import React, { Component } from 'react';
import {
    FormBinderWrapper,
    FormBinder,
    } from '@icedesign/form-binder';
import {
    Input,
    Grid,
} from '@icedesign/base';
import {injectIntl} from "react-intl";

class WaitOperation extends Component{

    constructor(props) {
        super(props);
        this.state = {
            value: this.props.currentOperation
        }
    }

    validateFormAndPost = () => {
        let noError = true;
        this.refs.form.validateAll((errors, values) => {
            if (errors != null) {
                noError = false;
            }
        });
        if (noError) {
            this.doSubmit(this.state.value);
        }
        // this.props.cancel();
    };

    doSubmit = (content) => {

        // this.props.submitWait(content);
    };

    componentWillReceiveProps(nextProps, nextContext) {
        // console.log("[Wait Operation] Operation props change!");
        if (nextProps.currentOperation !== this.props.currentOperation) {
            // this.validateFormAndPost();
            this.setState({
                value: nextProps.currentOperation
            })
        }
    }

    render() {
       return (
           <FormBinderWrapper
               value={this.state.value}
               ref="form">
               <FormBinder name="waitTime">
                  <Input style={{width: '60%'}} placeholder={this.props.intl.messages['test.wait.place']}> </Input>
               </FormBinder>
           </FormBinderWrapper>
       );
    }
}

export default injectIntl(WaitOperation);
