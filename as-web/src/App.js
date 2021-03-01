import React from 'react';
import './App.less';

import {Layout} from 'antd'
import DrawerMenu from "./component/DrawerMenu";
import Header from "./component/Header";
import {Switch, Route} from 'react-router-dom';
import Home from './page/Index'
import User from './page/user/Index'
import AddCategory from './page/category/add/Index'
import Category from './page/category/Index'
import ConfigList from './page/config/Index'
import ConfigAdd from './page/config/add/Index'


const {Content, Footer} = Layout;


class App extends React.Component {
    state = {
        title: '首页',
        collapsed: false
    };

    render() {
        const {collapsed, title} = this.state;
        return (
            <Layout className='App'>
                <DrawerMenu collapsed={collapsed} onMenuSelect={(pathname, title) => {
                    this.setState({
                        title
                    })
                }}/>
                <Layout>
                    <Header title={title} toggle={(collapsed) => {
                        this.setState({collapsed});
                    }}/>
                    <Content className='App-content'>
                        <Switch>
                            <Route exact path="/" component={Home}/>
                            <Route path="/category" component={Category}/>
                            <Route path="/category-add" component={AddCategory}/>
                            <Route path="/user" component={User}/>
                            <Route path="/config" component={ConfigList}/>
                            <Route path="/config-add" component={ConfigAdd}/>
                        </Switch>
                    </Content>
                    <Footer className='App-footer'>移动端架构师管理后台 ©2026 Created by
                        <a href='https://class.imooc.com/sale/mobilearchitect'> 移动端架构师实战体系课</a>
                    </Footer>
                </Layout>
            </Layout>
        );
    }
}

export default App;
