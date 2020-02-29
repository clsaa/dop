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
        color: '#999999'
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

class CaseParams extends Component{

    render() {
        return (
            <div>
                <Row style={{marginBottom: '10px'}}>
                    <Col style={{color: '#999999'}}>
                        {this.props.intl.messages['test.caseParams.title']}
                    </Col>
                    <Col>
                    </Col>

                    <Col>
                        <Button type="secondary" onClick={this.props.addItem}>
                            <Icon type="add" size='xxl'/>
                        </Button>
                    </Col>
                </Row>
                {this.props.caseParams.map((caseParam, index) => {
                    return (
                        <Row key={index} style={{marginBottom: '10px'}}>
                            <Col>
                                <span style={styles.formItemLabel}>name：</span>
                                <FormBinder required message={this.props.intl.messages['test.caseParams.nameWarn']} name={`caseParams[${index}].ref`}>
                                    <Input  style={{width: '60%'}}/>
                                </FormBinder>
                                <FormError name={`caseParams[${index}].ref`} />
                            </Col>
                            <Col>
                                <span style={styles.formItemLabel}>value：</span>
                                <FormBinder name={`caseParams[${index}].value`} required message={this.props.intl.messages['test.caseParams.valueWarn']}>
                                    <Input  style={{width: '60%'}}/>
                                </FormBinder>
                                <FormError name={`caseParams[${index}].value`} />
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
                    <Button type="secondary" onClick={this.props.submit} style={{marginTop: '20px'}}
                            disabled={this.props.disableSave}>
                        <Icon type="success" size='xxl'/>
                        {this.props.intl.messages['test.caseParams.saveText']}
                    </Button>
                </div>
            </div>
        );
    }
}

export default injectIntl(CaseParams);
