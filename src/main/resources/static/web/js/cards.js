const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            id: new URLSearchParams(location.search).get('id'),
            data: [],
            client: {},
            cards:[],
            credit:[],
            debit:[],
            transactionButtonClass: ["button-section", "d-flex", "align-items-center"],
        }
    },
    created() {
        this.getData();
    },
    mounted() {
        const body = document.querySelector("body"),
            sidebar = body.querySelector("nav"),
            toggle = body.querySelector(".toggle"),
            modeSwitch = body.querySelector(".toggle-switch"),
            modeText = body.querySelector(".mode-text"),
            mainContent = body.querySelector(".main-accounts")
            
        toggle.addEventListener("click", () => {
            sidebar.classList.toggle("close");
        });
        modeSwitch.addEventListener("click", () => {
            body.classList.toggle("dark");
            mainContent.classList.toggle("dark")

            if (body.classList.contains("dark")) {
                modeText.innerText = "Light mode";
            } else {
                modeText.innerText = "Dark mode";
            }
        });

    },
    methods: {
        getData() {
            axios.get(`/api/clients/current`)
                .then(response => {
                    this.data = response.data
                    return this.data
                })
                .then(data => {
                    this.client = data;
                    this.cards = this.client.cards;
                    this.credit = this.cards.filter(card => card.type == "Credit")
                    this.debit = this.cards.filter(card => card.type == "Debit")
                    console.log(this.credit);
                    console.log(this.debit);
                })
                .catch(error => console.error(error))
        },
        backOut(){
            this.transactionButtonClass.push("animate__animated");
            this.transactionButtonClass.push("animate__backOutRight");
        },
        logOut(){
            axios.post("/api/logout")
            .then((response) =>{
                console.log("signed out!!");
                window.location.href=response.request.responseURL
        })
        },
        deleteCard(number){
            Swal.fire({
                didOpen: (event) => {
                    document.querySelector('.swal2-confirm').addEventListener("click", () => {
                        console.log(number)
                        axios.delete("/api/clients/current/card", {
                            params:{
                                cardNumber: number
                            }
                        })
                        .then(data => {
                            console.log(data)
                            location.reload()
                        })
                        .catch(error => {
                            console.log(error);
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: error.response.data,
                                customClass: {
                                    popup: "text-white borderRadius-15",
                                },
                            })
                        })
                    })
                },
                icon: 'question',
                title: 'Are you sure?',
                text: `Delete card ${number}`,
            })
            
        },
        sweetModal() {
            Swal.fire({
                didOpen: (event) => {
                    document.querySelector('.swal2-confirm').addEventListener("click", () => {
                        this.createCard()
                    })
                },
                title: 'Create a card',
                html: `
                <form>
                <select class="type form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                    <option selected>Select your card type</option>
                    <option value="CREDIT">Credit</option>
                    <option value="DEBIT">Debit</option>
                </select>
                <select class="color form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                    <option selected>Select your card color</option>
                    <option value="SILVER">Silver</option>
                    <option value="GOLD">Gold</option>
                    <option value="TITANIUM">Titanium</option>
                </select>
                </form>
                `,
                inputPlaceholder: 'Select a ',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Create card',
                customClass: {
                    popup: "text-white borderRadius-15",
                },
                preConfirm: () => {
                    return [
                        this.type = document.querySelector('.type').value,
                        this.color = document.querySelector('.color').value,
                    ]
                }
            }).then((result) => {
            })
        },
        createCard() {
            axios.post('/api/clients/current/cards',
                "color=" + this.color +
                "&type=" + this.type)
                .then(response => {
                    if (response.status.toString() == "201") {
                        Swal.fire({
                            icon: 'success',
                            title: 'Created!!!',
                            text: response.data,
                            customClass: {
                                popup: "text-white borderRadius-15",
                            },
                        })
                    }

                })
                .catch(error => {
                    console.log(error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response.data,
                        customClass: {
                            popup: "text-white borderRadius-15",
                        },
                    })
                })
        },
    },
    computed: {
        
    }
}).mount('#app')