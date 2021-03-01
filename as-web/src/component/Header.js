import React from 'react';
import {MenuFoldOutlined, MenuUnfoldOutlined} from '@ant-design/icons';
import {Layout} from 'antd';
import './Header.less'
const {Header} = Layout;

export default class Index extends React.Component {
    state = {
        collapsed: false
    };
    toggle = () => {
        const {toggle} = this.props;
        const {collapsed} = this.state;
        toggle && toggle(!collapsed);
        this.setState({
            collapsed: !collapsed
        })
    };

    render() {
        const {collapsed} = this.state;
        const {title} = this.props;
        return (
            <Header className='header'>
                <div onClick={this.toggle}>
                    {collapsed ? <MenuUnfoldOutlined className='header-toggle'/> :
                        <MenuFoldOutlined className='header-toggle'/>}
                </div>
                <div className='header-title'>
                    <h1>{title}</h1>
                </div>
            </Header>
        );
    }

}
