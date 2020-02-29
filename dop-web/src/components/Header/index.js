import React, {Component} from 'react';
import {Link, withRouter} from 'react-router-dom';
import {Balloon, Icon} from '@icedesign/base';
import Menu, {SubMenu, Item as MenuItem} from '@icedesign/menu';
import FoundationSymbol from 'foundation-symbol';
import IceImg from '@icedesign/img';
import {headerMenuConfig} from '../../menuConfig';
import Logo from '../Logo/index';
import './index.scss';
import {FormattedMessage, IntlProvider} from 'react-intl';
import logo from './images/logo.png'

// @withRouter
class Header extends Component {
    static propTypes = {};

    static defaultProps = {};

    constructor(props) {
        super(props);
        this.state = {
            user: {
                name: "loading",
            },
            language: "简体中文"
        };
    }

    componentWillMount() {
        let user = {
            name: window.sessionStorage.getItem('user-name')
        };

        this.setState({
            user: user,
            language: window.sessionStorage.getItem('language') === 'zh-CN' ? '简体中文' : 'English'
        });
    }

    changeLanguage(language) {
        let rlanguage;
        switch (language) {
            case "简体中文":
                rlanguage = 'zh-CN';
                break;
            case "English":
                rlanguage = 'en-US';
                break;
            default:
                rlanguage = 'zh-CN';
        }
        window.sessionStorage.setItem('language', rlanguage);
        this.setState({
            language: window.sessionStorage.getItem('language') === 'zh-CN' ? '简体中文' : 'English'
        });
    }

    logout() {
        window.sessionStorage.clear();
    }

    render() {
        const {location = {}} = this.props;
        const {pathname} = location;
        return (
            <div className="header-container">
                <div className="header-content">
                    <Logo isDark img/>
                    <div className="header-navbar">
                        <Menu
                            className="header-navbar-menu"
                            onClick={this.handleNavClick}
                            selectedKeys={[pathname]}
                            defaultSelectedKeys={[pathname]}
                            mode="horizontal"
                        >
                            {headerMenuConfig &&
                            headerMenuConfig.length > 0 &&
                            headerMenuConfig.map((nav, index) => {
                                if (nav.children && nav.children.length > 0) {
                                    return (
                                        <SubMenu
                                            triggerType="click"
                                            key={index}
                                            title={
                                                <span>
                            {nav.icon ? (
                                <FoundationSymbol size="small" type={nav.icon}/>
                            ) : null}
                                                    <span>{nav.name}</span>
                          </span>
                                            }
                                        >
                                            {nav.children.map((item) => {
                                                const linkProps = {};
                                                if (item.external) {
                                                    if (item.newWindow) {
                                                        linkProps.target = '_blank';
                                                    }

                                                    linkProps.href = item.path;
                                                    return (
                                                        <MenuItem key={item.path}>
                                                            <a {...linkProps}>
                                                                <span>{item.name}</span>
                                                            </a>
                                                        </MenuItem>
                                                    );
                                                }
                                                linkProps.to = item.path;
                                                return (
                                                    <MenuItem key={item.path}>
                                                        <Link {...linkProps}>
                                                            <span>{item.name}</span>
                                                        </Link>
                                                    </MenuItem>
                                                );
                                            })}
                                        </SubMenu>
                                    );
                                }
                                const linkProps = {};
                                if (nav.external) {
                                    if (nav.newWindow) {
                                        linkProps.target = '_blank';
                                    }
                                    linkProps.href = nav.path;
                                    return (
                                        <MenuItem key={index}>
                                            <a {...linkProps}>
                          <span>
                            {nav.icon ? (
                                <FoundationSymbol size="small" type={nav.icon}/>
                            ) : null}
                              {nav.name}
                          </span>
                                            </a>
                                        </MenuItem>
                                    );
                                }
                                linkProps.to = nav.path;
                                return (
                                    <MenuItem key={nav.path}>
                                        <Link {...linkProps}>
                        <span>
                          {nav.icon ? (
                              <FoundationSymbol size="small" type={nav.icon}/>
                          ) : null}
                            {nav.name}
                        </span>
                                        </Link>
                                    </MenuItem>
                                );
                            })}
                        </Menu>
                        <Balloon
                            triggerType="hover"
                            trigger={
                                <div
                                    className="ice-design-header-userpannel"
                                    style={{
                                        display: 'flex',
                                        alignItems: 'center',
                                        fontSize: 12,
                                    }}
                                >
                                    <div className="user-profile">
                                        <span className="user-name" style={{fontSize: '13px'}}>
                                            {this.state.language}
                                        </span>
                                    </div>
                                    <Icon
                                        type="arrow-down-filling"
                                        size="xxs"
                                        className="icon-down"
                                    />
                                </div>
                            }
                            closable={false}
                            className="user-profile-menu"
                        >
                            <ul>
                                <li className="user-profile-menu-item" onClick={this.changeLanguage.bind(this, "简体中文")}>
                                    简体中文
                                </li>
                                <li className="user-profile-menu-item"
                                    onClick={this.changeLanguage.bind(this, "English")}>
                                    English
                                </li>
                            </ul>
                        </Balloon>
                        <Balloon
                            triggerType="hover"
                            trigger={
                                <div
                                    className="ice-design-header-userpannel"
                                    style={{
                                        display: 'flex',
                                        alignItems: 'center',
                                        fontSize: 12,
                                    }}
                                >
                                    <IceImg
                                        height={40}
                                        width={40}
                                        src={require('./images/avatar.png')}
                                        className="user-avatar"
                                    />
                                    <div className="user-profile">
                                        <span className="user-name" style={{fontSize: '13px'}}>
                                            {this.state.user.name}
                                        </span>
                                    </div>
                                    <Icon
                                        type="arrow-down-filling"
                                        size="xxs"
                                        className="icon-down"
                                    />
                                </div>
                            }
                            closable={false}
                            className="user-profile-menu"
                        >
                            <ul>
                                <li className="user-profile-menu-item">
                                    <Link to="/">
                                        <FoundationSymbol type="person" size="small"/>
                                        <FormattedMessage
                                            id="base.myHome"
                                            defaultMessage="我的主页"
                                        />
                                    </Link>
                                </li>

                                <li className="user-profile-menu-item">
                                    <Link to="/">
                                        <FoundationSymbol type="repair" size="small"/>
                                        <FormattedMessage
                                            id="base.setting"
                                            defaultMessage="设置"
                                        />
                                    </Link>
                                </li>
                                <li className="user-profile-menu-item" onClick={this.logout.bind(this)}>
                                    <Link to="/login">
                                        <FoundationSymbol type="compass" size="small"/>
                                        <FormattedMessage
                                            id="base.logout"
                                            defaultMessage="退出"
                                        />
                                    </Link>
                                </li>
                            </ul>
                        </Balloon>
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(Header)


