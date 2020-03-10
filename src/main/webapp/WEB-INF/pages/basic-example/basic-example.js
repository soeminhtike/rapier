/* var config = {
        container: "#basic-example",

        connectors: {
            type: 'step'
        },
        node: {
            HTMLclass: 'nodeExample1'
        }
    },
    ceo = {
        text: {
            name: "Mark Hill",
            title: "Chief executive officer",
            contact: "Tel: 01 213 123 134",
        },
        image: "../headshots/2.jpg"
    },

    cto = {
        parent: ceo,
        text:{
            name: "Joe Linux",
            title: "Chief Technology Officer",
        },
        stackChildren: true,
        image: "../headshots/1.jpg"
    },
    cbo = {
        parent: ceo,
        stackChildren: true,
        text:{
            name: "Linda May",
            title: "Chief Business Officer",
        },
        image: "../headshots/5.jpg"
    },
    cdo = {
        parent: ceo,
        text:{
            name: "John Green",
            title: "Chief accounting officer",
            contact: "Tel: 01 213 123 134",
        },
        image: "../headshots/6.jpg"
    },
    cio = {
        parent: cto,
        text:{
            name: "Ron Blomquist",
            title: "Chief Information Security Officer"
        },
        image: "../headshots/8.jpg"
    },
    ciso = {
        parent: cto,
        text:{
            name: "Michael Rubin",
            title: "Chief Innovation Officer",
            contact: {val: "we@aregreat.com", href: "mailto:we@aregreat.com"}
        },
        image: "../headshots/9.jpg"
    },
    cio2 = {
        parent: cdo,
        text:{
            name: "Erica Reel",
            title: "Chief Customer Officer"
        },
        link: {
            href: "http://www.google.com"
        },
        image: "../headshots/10.jpg"
    },
    ciso2 = {
        parent: cbo,
        text:{
            name: "Alice Lopez",
            title: "Chief Communications Officer"
        },
        image: "../headshots/7.jpg"
    },
    ciso3 = {
        parent: cbo,
        text:{
            name: "Mary Johnson",
            title: "Chief Brand Officer"
        },
        image: "../headshots/4.jpg"
    },
    ciso4 = {
        parent: cbo,
        text:{
            name: "Kirk Douglas",
            title: "Chief Business Development Officer"
        },
        image: "../headshots/11.jpg"
    }

    chart_config = [
        config,
        ceo,
        cto,
        cbo,
        cdo,
        cio,
        ciso,
        cio2,
        ciso2,
        ciso3,
        ciso4
    ];

*/


    // Another approach, same result
    // JSON approach


    var chart_config = {
        chart: {
            container: "#basic-example",

            connectors: {
                type: 'step'
            },
            node: {
                HTMLclass: 'nodeExample1'
            }
        },
        nodeStructure: {
          text:{name:"##",criteria:"null"}, children:[{text:{name:"Repeat",criteria:"entry"}, children:[{text:{name:"Attendance",criteria:"1"}, children:[{text:{name:"Q18",criteria:"1"}, children:[{text:{name:"",criteria:"1"}, children:[{text:{name:"Zero --- ",criteria:"Zero ---"}, children:[]}]},{text:{name:"Q5",criteria:"2"}, children:[{text:{name:"2",criteria:"1"}, children:[]},{text:{name:"",criteria:"2"}, children:[{text:{name:"Zero --- ",criteria:"Zero ---"}, children:[]}]}]}]},{text:{name:"Q1",criteria:"2"}, children:[{text:{name:"2",criteria:"1"}, children:[]},{text:{name:"1",criteria:"2"}, children:[]}]}]},{text:{name:"Difficulty",criteria:"2"}, children:[{text:{name:"Class",criteria:"1"}, children:[{text:{name:"3",criteria:"1"}, children:[]},{text:{name:"2",criteria:"2"}, children:[]}]},{text:{name:"Class",criteria:"2"}, children:[{text:{name:"Attendance",criteria:"1"}, children:[{text:{name:"1",criteria:"1"}, children:[]},{text:{name:"3",criteria:"2"}, children:[]}]},{text:{name:"3",criteria:"2"}, children:[]}]}]}]}]

      }
    };
