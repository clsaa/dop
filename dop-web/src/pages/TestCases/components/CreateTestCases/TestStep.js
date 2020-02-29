import React, { Component } from 'react';
import {  FormBinder, FormError } from '@icedesign/form-binder';
import {
    Button, Grid, Input
}
    from "@icedesign/base";
import IceContainer from '@icedesign/container';
import {injectIntl, FormattedMessage} from 'react-intl';

const { Row, Col } = Grid;

class TestSteps extends Component {
    render() {
        return (
            <IceContainer>
                {this.props.testSteps.map((testStep, index) => {
                    return (
                        <Row key={index} >
                            <Col>
                                <FormBinder required message={this.props.intl.messages['test.caseLists.add.manual.dialog.testSteps.descWarn']} name={`testSteps[${index}].stepDesc`} >
                                    <Input multiple placeholder={this.props.intl.messages['test.caseLists.add.manual.dialog.testSteps.desc']+(index+1)} style={{width: '100%'}}/>
                                    {/*<TextArea />*/}
                                </FormBinder>
                                <FormError name={`testSteps[${index}].stepDesc`} style={styles.formError} />
                            </Col>
                            &nbsp;
                            <Col>
                                <FormBinder name={`testSteps[${index}].expectResult`} required message={this.props.intl.messages['test.caseLists.add.manual.dialog.testSteps.resultWarn']} >
                                    <Input multiple placeholder={this.props.intl.messages['test.caseLists.add.manual.dialog.testSteps.result']} style={{width: '100%'}}/>
                                    {/*<TextArea/>*/}
                                </FormBinder>
                                <FormError name={`testSteps[${index}].expectResult`} style={styles.formError} />
                            </Col>
                            <Button type="secondary" style={{margin: 'auto', marginLeft: '10px'}} onClick={this.props.removeItem.bind(this, index)}>{this.props.intl.messages['test.caseLists.add.manual.dialog.testSteps.delete']}</Button>
                        </Row>
                    );
                })}
                <div style={styles.buttons}>
                    <Button type="secondary" onClick={this.props.addItem}>
                        {this.props.intl.messages['test.caseLists.add.manual.dialog.testSteps.add']}
                    </Button>
                </div>
            </IceContainer>
        );
    }
}

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
export default injectIntl(TestSteps);
