import React, { Component } from 'react';
import {
    Input,
    Button,
    Grid, Icon,
} from '@icedesign/base';

import {  FormBinder, FormError } from '@icedesign/form-binder';
import {injectIntl} from "react-intl";

const { Row, Col } = Grid;

const styles = {
    formItem: {
        marginBottom: '20px',
        display: 'flex',
        alignItems: 'center',
    },
    formItemLabel: {
        width: '70px',
        marginRight: '10px',
        display: 'inline-block',
        textAlign: 'right',
    },
    formItemError: {
        marginLeft: '10px',
    },
    preview: {
        border: '1px solid #eee',
        marginTop: 20,
        padding: 10
    }
};

class ResultParam extends Component{

    render() {
        return (
            <div>
                {this.props.resultParams.map((resultParam, index) => {
                    return (
                        <Row key={index} style={{marginBottom: '10px'}}>
                            <Col>
                                <span style={styles.formItemLabel}>{this.props.intl.messages['test.requestHeader.name']}</span>
                                <FormBinder required message="参数名称必填" name={`resultParams[${index}].name`} >
                                    <Input />
                                </FormBinder>
                                <FormError name={`resultParams[${index}].name`} />
                            </Col>
                            &nbsp;
                            <Col>
                                <span style={styles.formItemLabel}>{this.props.intl.messages['test.requestHeader.value']}</span>
                                <FormBinder name={`resultParams[${index}].rawValue`} required message="参数表达式必填" >
                                    <Input/>
                                </FormBinder>
                                <FormError name={`resultParams[${index}].rawValue`} />
                            </Col>
                            <Col>
                                <Button type="secondary" onClick={this.props.removeItem.bind(this, index)}>
                                    <Icon type="ashbin" size='xxxl'/>
                                </Button>
                            </Col>
                        </Row>
                    );
                })}
                <div style={styles.buttons}>
                    <Button type="secondary" onClick={this.props.addItem}>
                        <Icon type="add" size='xxl'/>
                    </Button>
                </div>
            </div>
        );
    }
}

export default injectIntl(ResultParam);
