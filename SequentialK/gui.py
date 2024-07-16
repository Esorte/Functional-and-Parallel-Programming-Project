import tkinter as tk
from tkinter import simpledialog, ttk, messagebox
import json

# Initialize the board with comboboxes for each cell
def initialize_board(size):
    root.columnconfigure(0, weight=1)
    root.columnconfigure(1, weight=1)  
    root.columnconfigure(2, weight=1) 

    board_frame = tk.Frame(root)
    board_frame.grid(row=0, column=1, sticky="nsew", padx=(50, 0))

    root.rowconfigure(0, weight=1) 

    square_font = ('Courier', 9)  
    combobox_width = 3  
    
    for j in range(size):
        col_label = tk.Label(board_frame, text=str(j), font=square_font)
        col_label.grid(row=0, column=j+1, padx=0, pady=0)

    for i in range(size):
        row_label = tk.Label(board_frame, text=str(i), font=square_font)
        row_label.grid(row=i+1, column=0, padx=0, pady=0)

        for j in range(size):
            num_var = tk.StringVar(root)
            dropdown = ttk.Combobox(board_frame, textvariable=num_var, values=[str(n) for n in range(1, size+1)],
                                    width=combobox_width, font=square_font)
            dropdown.grid(row=i+1, column=j+1, padx=0, pady=0)
            board_vars[(i, j)] = num_var

# Add a constraint between two cells
def add_constraint():
    cell1 = simpledialog.askstring("Add Constraint", "Enter first cell (row,col), starting from 0,0:")
    cell2 = simpledialog.askstring("Add Constraint", "Enter second cell (row,col), starting from 0,0:")
    try:
        cell1_tuple = tuple(map(int, cell1.strip().split(',')))
        cell2_tuple = tuple(map(int, cell2.strip().split(',')))
        constraints.append((cell1_tuple, cell2_tuple))
        update_constraints_listbox()
    except ValueError:
        messagebox.showerror("Error", "Invalid input. Please enter the cells in the format row,col.")

# Update the listbox displaying the constraints
def update_constraints_listbox():
    constraints_listbox.delete(0, tk.END)
    for constraint in constraints:
        display_text = f"{constraint}"
        constraints_listbox.insert(tk.END, display_text)

# Function to remove a selected constraint
def remove_constraint():
    selection = constraints_listbox.curselection()
    if not selection:
        messagebox.showerror("Error", "Please select a constraint to remove.")
        return
    selected_index = selection[0]
    constraints.pop(selected_index)
    update_constraints_listbox() 

# Finalize the puzzle setup and save to a JSON file
def finish():
    initial_digits_str_keys = {str(k): int(v.get()) for k, v in board_vars.items() if v.get() != "0" and v.get() != ""}
    data = {
        "board_size": board_size,
        "initial_digits": initial_digits_str_keys,
        "constraints": constraints
    }
    # print(data)
    
    with open('puzzle_data.json', 'w') as outfile:
        json.dump(data, outfile)
    
    root.destroy()

# Main application setup
root = tk.Tk()
root.title("Futoshiki Puzzle Initializer")
root.minsize(400, 300)

board_size = simpledialog.askinteger("Input", "What is the board size?", minvalue=4, maxvalue=9)
board_vars = {}
constraints = []

initialize_board(board_size)

root.columnconfigure(0, weight=1)
root.columnconfigure(1, weight=1)
root.columnconfigure(2, weight=1)

root.grid_rowconfigure(0, weight=1)  
root.grid_rowconfigure(1, weight=0)  
root.grid_rowconfigure(2, weight=1)  

controls_frame = tk.Frame(root)
controls_frame.grid(row=1, column=0, columnspan=3, sticky="nsew", padx=10, pady=10) 

add_constraint_frame = tk.Frame(controls_frame)
add_constraint_frame.grid(row=0, column=0, sticky="ew")
controls_frame.columnconfigure(0, weight=1)

add_constraint_button = tk.Button(add_constraint_frame, text="Add Constraint", command=add_constraint)
add_constraint_button.pack(side="top", padx=10, pady=5)

remove_constraint_button = tk.Button(add_constraint_frame, text="Remove Constraint", command=remove_constraint)
remove_constraint_button.pack(side="top", padx=10, pady=5)

constraints_listbox_frame = tk.Frame(controls_frame)
constraints_listbox_frame.grid(row=1, column=0, sticky="ew")

constraints_listbox = tk.Listbox(constraints_listbox_frame)
constraints_listbox.pack(side="top", fill="x", expand=True, padx=10, pady=5)

finish_button_frame = tk.Frame(controls_frame)
finish_button_frame.grid(row=2, column=0, sticky="ew")

finish_button = tk.Button(finish_button_frame, text="Finish", command=finish)
finish_button.pack(side="top", padx=10, pady=5)

# Adjusted frame packing to ensure enough space for buttons
add_constraint_frame.pack_propagate(False)
constraints_listbox_frame.pack_propagate(False)
finish_button_frame.pack_propagate(False)

# Adjusted frame sizes to ensure buttons are not squished
add_constraint_frame.config(height=100) 
constraints_listbox_frame.config(width=400, height=100)
finish_button_frame.config(width=400, height=50)

root.mainloop()